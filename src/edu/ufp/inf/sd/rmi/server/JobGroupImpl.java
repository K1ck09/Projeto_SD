package edu.ufp.inf.sd.rmi.server;

import edu.ufp.inf.sd.rmi.client.JobControllerRI;
import edu.ufp.inf.sd.rmi.client.WorkerRI;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI {
    private String jobName;
    private String owner;
    private String reward;
    private String strat;
    private State state;
    private String workLoad;
    private Integer totalShares = 0;
    private String filePath;
    private boolean paid = false;
    Integer idSize = 0;
    UserSessionRI client;

    Map<Integer, WorkerRI> jobWorkers = new HashMap<>();
    ArrayList<WorkerRI> bestCombination = new ArrayList<>();

    private static final String FILE_PATH = "C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\rmi\\server\\files\\";
    private HashMap<String, JobControllerRI> list = new HashMap<>();

    protected JobGroupImpl(UserSessionRI client, String jobName, String owner, String strat,String reward, String workLoad) throws RemoteException {
        this.client = client;
        this.jobName = jobName;
        this.owner = owner;
        this.strat = strat;
        this.reward = reward;
        this.state = new State("Available", this.jobName);
        this.workLoad = workLoad;
    }

    @Override
    public synchronized void updateTotalShares(WorkerRI worker) throws IOException {
        if(this.state.getCurrentState().compareTo("OnGoing")==0 || this.state.getCurrentState().compareTo("Available")==0){
            if(worker.getState().getCurrentState().compareTo("Available") == 0){
                worker.changeState("Ongoing");
            }
            if (bestCombination.isEmpty() ) {
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
                    System.out.println("[USER] "+this.client.findUser(bestCombination.get(0).getOwner().getUsername()).getUsername()+
                            " [CREDITS] "+this.client.findUser(bestCombination.get(0).getOwner().getUsername()).getCredits());
                    jobWorkers.get(bestCombination.get(0).getId()).setTotalRewarded(10);
                    this.paid = true;
                }
                notifyAllWorkers("Stopped");
            }
        }else if(this.state.getCurrentState().compareTo("Paused")==0) {
            for (WorkerRI w : jobWorkers.values()) {
                w.changeState("Paused");
            }
            updateList();
            this.client.updateMenus();
        }
    }

    @Override
    public boolean attachWorker(WorkerRI worker) throws IOException {
        if (jobWorkers.size() == 0) {
            this.state.setCurrentState("OnGoing");
        }
        if (this.state.getCurrentState().compareTo("Available") == 0 || this.state.getCurrentState().compareTo("OnGoing") == 0) {
            jobWorkers.put(worker.getId(), worker);
            idSize++;
            worker.setFile(filePath);
            updateList();
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
        if(state.compareTo("OnGoing")==0){
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

    @Override
    public String getJobReward() {
        return reward;
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
