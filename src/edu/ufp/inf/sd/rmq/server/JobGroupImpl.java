package edu.ufp.inf.sd.rmq.server;

import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rmq.client.JobController;
import edu.ufp.inf.sd.rmq.client.JobControllerRI;
import edu.ufp.inf.sd.rmq.client.WorkerRI;
import edu.ufp.inf.sd.rmq.util.RabbitUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI {
    private String jobName;
    private String owner;
    private String strat;
    private String crossStrat;
    private String reward;
    private State state;
    private String workLoad;
    private String timer;
    private Integer totalShares = 0;
    private String filePath;
    Map<Integer, WorkerRI> jobWorkers = new HashMap<>();
    ArrayList<WorkerRI> bestCombination = new ArrayList<>();
    private boolean paid = false;
    UserSessionRI client;
    Integer idSize = 0;
    private static final String HOST="localhost";
    private static final Integer PORT=5672;
    private static final String ROUTING_KEY="";
    private Channel channel;
    private String exchangeName;
    private final static long JOB_WORK_TIME=10000;

    private static final String FILE_PATH = "C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\rmq\\server\\files\\";
    private HashMap<String, JobControllerRI> list = new HashMap<>();

    protected JobGroupImpl(UserSessionRI client, String jobName, String owner, String strat, String reward, String optional) throws RemoteException {
        this.client = client;
        this.jobName = jobName;
        this.owner = owner;
        this.strat = strat;
        if(strat.compareTo("TabuSearch")==0){
            this.state = new State("Available", this.jobName);
            this.workLoad = optional;
            this.reward = reward;
        }else {
            this.state = new State("Waiting", this.jobName);
            this.timer = optional;
            Connection connection = null;
            try {
                connection = RabbitUtils.newConnection2Server(HOST, PORT, "guest", "guest");
                assert connection != null;
                this.channel = RabbitUtils.createChannel2Server(connection);
                exchangeName = jobName;
                channel.exchangeDeclare(exchangeName,BuiltinExchangeType.FANOUT);
                /**
                 * Creates a thread Timer, thread will act as a temporizer, will sleep for the specified time
                 * And call start function in the end.
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        assert timer != null;
                        try {
                            Thread.sleep(Integer.parseInt(timer)* 1000L);
                            jobStart();
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    private void jobStart() throws IOException {
        String msg="start,Ongoing";
        channel.basicPublish(exchangeName, ROUTING_KEY, null, msg.getBytes(StandardCharsets.UTF_8));
        state.setCurrentState("OnGoing");
        new Thread(new Runnable() {
            @Override
            public void run(){
                assert timer != null;
                try {
                    Thread.sleep(JOB_WORK_TIME);
                    jobStop();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void jobStop() throws IOException {
        String msg="stop";
        channel.basicPublish(exchangeName, ROUTING_KEY, null, msg.getBytes(StandardCharsets.UTF_8));
        endJob();
    }

    private void endJob() throws RemoteException {
        state.setCurrentState("Finished");
        this.client.setCredits(bestCombination.get(0).getOwner(), 10);
        jobWorkers.get(bestCombination.get(0).getId()).setTotalRewarded(10);
        this.paid = true;

    }

    private void receiveResults(WorkerRI worker) throws IOException {
        String workerQueue =jobName+"_serverResults_"+ worker.getId() +"_"+worker.getOwner().getUsername();
        channel.queueDeclare(workerQueue,false,false,false,null);
        DeliverCallback receiveResults=(consumerTag, delivery) -> {
            String message=new String(delivery.getBody(), StandardCharsets.UTF_8);
            if (bestCombination.isEmpty()) {
                bestCombination.add(worker);
            } else {
                if (bestCombination.get(0).getBestMakespan() > worker.getBestMakespan()) {
                    bestCombination.remove(0);
                    bestCombination.add(worker);
                }
                if (this.totalShares < Integer.parseInt(this.workLoad) && worker.getState().getCurrentState().compareTo("Ongoing") == 0) {
                    this.totalShares++;
                    updateList();
                    this.client.updateMenus();//deixar se não causar lag
                    worker.setTotalShares(worker.getTotalShares() + 1);
                }
            }

            updateList();
        };
        CancelCallback cancelCallback=(consumerTag) ->{
            System.out.println(" [0] Consumer Tag [" + consumerTag + "] - Cancel Callback invoked");
        };

        //o nome da queue é o que junta a queue a callback
        channel.basicConsume(workerQueue, true, receiveResults, cancelCallback);
    }
    @Override
    public synchronized void updateTotalShares(WorkerRI worker) throws IOException {
        if (this.state.getCurrentState().compareTo("OnGoing") == 0 || this.state.getCurrentState().compareTo("Available") == 0) {
            if (worker.getState().getCurrentState().compareTo("Available") == 0) {
                worker.changeState("Ongoing");
            }
            if (bestCombination.isEmpty()) {
                bestCombination.add(worker);
            } else {
                if (bestCombination.get(0).getBestMakespan() > worker.getBestMakespan()) {
                    bestCombination.remove(0);
                    bestCombination.add(worker);
                }
            }
            //System.out.println("["+worker.getId()+"] -> "+worker.getTotalShares());
            //System.out.println("Shares - "+this.totalShares+" Workload - "+workLoad);
            if (this.totalShares < Integer.parseInt(this.workLoad) && worker.getState().getCurrentState().compareTo("Ongoing") == 0) {
                this.totalShares++;
                updateList();
                this.client.updateMenus();//deixar se não causar lag
                worker.setTotalShares(worker.getTotalShares() + 1);
                worker.setTotalRewarded(1);
                worker.setOperation();
            } else if (this.totalShares < Integer.parseInt(this.workLoad) && worker.getState().getCurrentState().compareTo("Paused") == 0) {
                updateList();
            } else {
                this.state.setCurrentState("Finished");
                if (!paid) {
                    this.client.setCredits(bestCombination.get(0).getOwner(), 10);
                    jobWorkers.get(bestCombination.get(0).getId()).setTotalRewarded(10);
                    this.paid = true;
                }
                notifyAllWorkers("Stopped");
            }
        } else if (this.state.getCurrentState().compareTo("Paused") == 0) {
            for (WorkerRI w : jobWorkers.values()) {
                w.changeState("Paused");
            }
            updateList();
            this.client.updateMenus();
        }
    }

    @Override
    public boolean attachWorker(WorkerRI worker) throws IOException {
        if (jobWorkers.size() == 0 && strat.compareTo("TabuSearch")==0) {
            this.state.setCurrentState("OnGoing");
        }
        if (this.state.getCurrentState().compareTo("Available") == 0 || this.state.getCurrentState().compareTo("OnGoing") == 0
                || this.state.getCurrentState().compareTo("Waiting") == 0) {
            if (this.strat.compareTo("TabuSearch") == 0) {
                jobWorkers.put(worker.getId(), worker);
                idSize++;
                worker.setFile(filePath);
                updateList();
            } else if (this.strat.compareTo("Genetic Algorithm") == 0) {
                //file, CrossStrat,
                worker.setState("Waiting");
                String msg = "download" + "," +filePath;
                channel.basicPublish(exchangeName, ROUTING_KEY, null, msg.getBytes(StandardCharsets.UTF_8));
                jobWorkers.put(worker.getId(), worker);
                idSize++;
                receiveResults(worker);
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateList() throws IOException {
        for (JobControllerRI j : list.values()) {
            j.updateGUI();
        }
    }

    @Override
    public void removeWorker(WorkerRI selectedWorker) throws IOException {
        if(bestCombination.get(0).getId().equals(selectedWorker.getId())){
            bestCombination.clear();
        }
        this.client.setCredits(this.client.getUser(),selectedWorker.getTotalShares());
        this.jobWorkers.remove(selectedWorker.getId());
        updateList();
    }

    @Override
    public Integer getIdsSize() {
        return idSize;
    }

    @Override
    public void setState(String state) throws IOException {
        this.state.setCurrentState(state);
        if (state.compareTo("OnGoing") == 0) {
            for (WorkerRI w : jobWorkers.values()) {
                w.setState("Ongoing");
            }
            updateList();
            this.client.updateMenus();
        }
    }

    @Override
    public boolean removeAllWorkers() throws IOException {
        this.state.setCurrentState("Deleted");
        this.client.setCredits(bestCombination.get(0).getOwner(), bestCombination.get(0).getTotalShares());
        jobWorkers.get(bestCombination.get(0).getId()).setTotalRewarded(bestCombination.get(0).getTotalShares());
        this.paid = true;
        return true;
    }

    private void notifyAllWorkers(String state) throws IOException {
        for (WorkerRI w : jobWorkers.values()) {
            this.client.setCredits(w.getOwner(),w.getTotalShares());
            System.out.println("[USER] "+this.client.findUser(w.getOwner().getUsername()).getUsername()+
                    " [CREDITS] "+this.client.findUser(w.getOwner().getUsername()).getCredits());
            w.changeState(state);
        }
        updateList();
        this.client.updateMenus();
    }

    @Override
    public void addToList(JobControllerRI jobController, String user) {
        this.list.put(user, jobController);
    }

    @Override
    public void removeFromList(String username) {
        this.list.remove(username);
    }

    @Override
    public String getBestResut() throws RemoteException {
        if (bestCombination.size() != 0) {
            return String.valueOf(bestCombination.get(0).getBestMakespan());
        }
        return "-";
    }

    @Override
    public WorkerRI getBestResult() {
        if (bestCombination.size() != 0) {
            return bestCombination.get(0);
        }
        return null;
    }

    @Override
    public Integer getTotalShares() {
        return totalShares;
    }

    @Override
    public String getTimer() {
        return timer;
    }

    @Override
    public void uploadFile(byte[] mydata) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        File file = new File(FILE_PATH + getJobOwner() + jobName + dtf.format(now));
        filePath = file.getAbsolutePath();
        FileOutputStream out = new FileOutputStream(file);
        out.write(mydata);
        out.flush();
        out.close();
    }

    @Override
    public byte[] downloadFileFromServer(String serverpath) throws IOException {
        byte[] mydata;

        File serverpathfile = new File(serverpath);
        mydata = new byte[(int) serverpathfile.length()];
        FileInputStream in;
        in = new FileInputStream(serverpathfile);
        in.read(mydata, 0, mydata.length);
        in.close();
        return mydata;
    }

    @Override
    public Map<Integer, WorkerRI> getJobWorkers() throws RemoteException {
        return jobWorkers;
    }

    @Override
    public String getWorkload() {
        return workLoad;
    }

    @Override
    public Integer getWorkersSize() {
        return jobWorkers.size();
    }

    @Override
    public String getJobName() {
        return jobName;
    }

    @Override
    public String getJobOwner() {
        return owner;
    }

    @Override
    public String getJobStrat() {
        return strat;
    }

    @Override
    public String getJobReward() {
        return reward;
    }

    @Override
    public State getJobState() {
        return state;
    }

    @Override
    public String getState() throws RemoteException {
        return state.getCurrentState();
    }

    public void printHashMap(Map<Integer, WorkerRI> hashMap) throws RemoteException {
        Collection<WorkerRI> workers = hashMap.values();
        for (WorkerRI worker : workers) {
            System.out.println("value: " + worker.getId());
        }
    }

}
