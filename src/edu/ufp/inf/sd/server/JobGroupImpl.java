package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.JobController;
import edu.ufp.inf.sd.client.JobControllerRI;
import edu.ufp.inf.sd.client.JobShopClient;
import edu.ufp.inf.sd.client.WorkerRI;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI {
    private String jobName;
    private String owner;
    private String strat;
    private String reward;
    private State state;
    private String workLoad;
    private Integer totalShares=0;
    private File file;
    private String filePath;
    Map<Integer, WorkerRI> jobWorkers = new HashMap<>();
    ArrayList<WorkerRI> bestCombination = new ArrayList<>();
    private boolean paid = false;
    JobController jobController=null;
   // private JobThread jobThread;
   UserSessionRI client;

    private static final String FILE_PATH = "C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\server\\files\\";
    private ArrayList<JobControllerRI> list=new ArrayList<>();

    protected JobGroupImpl(UserSessionRI client,String jobName, String owner, String strat, String reward, String workLoad) throws RemoteException {
        this.client=client;
        //this.jobThread=new JobThread();
        this.jobName = jobName;
        this.owner = owner;
        this.strat = strat;
        this.reward = reward;
        this.state = new State("Available",this.jobName);
        this.workLoad = workLoad;
    }

    public JobGroupImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void updateTotalShares(int totalShares, WorkerRI worker) throws IOException {
        if(bestCombination.isEmpty()){
            bestCombination.add(worker);
        }else{
            if(bestCombination.get(0).getBestMakespan()>worker.getBestMakespan()){
                bestCombination.remove(0);
                bestCombination.add(worker);
            }
        }
        //System.out.println("["+worker.getId()+"] -> "+worker.getTotalShares());
        //System.out.println("Shares - "+this.totalShares+" Workload - "+workLoad);
        if(this.totalShares<Integer.parseInt(this.workLoad) && worker.getState().getCurrentState().compareTo("StandBy")==0){
            this.totalShares++;
            updateList();
            //this.client.updateMenus();//deixar se não causar lag
            worker.setTotalShares(worker.getTotalShares()+1);
            worker.setOperation();
        }else{
            this.state.setCurrentState("Finished");
            if(!paid){
                this.client.setCredits(bestCombination.get(0).getOwner(),Integer.parseInt(this.reward));
                jobWorkers.get(bestCombination.get(0).getId()).setTotalRewarded(Integer.parseInt(this.reward));
                this.paid=true;
            }
            notifyAllWorkers();
        }
    }

    @Override
    public void addToList(JobControllerRI jobController) throws IOException{
        list.add(jobController);
    }

    private void updateList() throws IOException {
        for(JobControllerRI j:list){
            j.updateGUI();
        }
    }
    private void notifyAllWorkers() throws IOException {
        for(WorkerRI w :jobWorkers.values()){
            w.updateWorkerController();
        }
        updateList();
        this.client.updateMenus();
    }

    @Override
    public boolean attachWorker(WorkerRI worker) throws RemoteException,IOException {
        if(jobWorkers.size()==0){
            this.state.setCurrentState("OnGoing");
        }
        if(this.state.getCurrentState().compareTo("Available")==0 || this.state.getCurrentState().compareTo("OnGoing")==0 ){
            jobWorkers.put(worker.getId(),worker);
            //Job Threadvariaveis todas a 0/null;
           // Thread t=new Thread(jobThread);
           // System.out.println("starting THREAD");
           // t.start();
            worker.setOperation(filePath);
            updateList();
            return true;
        }
        return false;
    }

    public String getFilePath() {
        return filePath;
    }
    public void setTotalShares(Integer totalShares) {
        this.totalShares = totalShares;
    }
    @Override
    public String getBestResut() throws RemoteException {
        if(bestCombination.size()!=0){
            return String.valueOf(bestCombination.get(0).getBestMakespan());
        }
        return "-";
    }

    @Override
    public WorkerRI getBestResult() throws RemoteException {
        if(bestCombination.size()!=0){
            return bestCombination.get(0);
        }
        return null;
    }
    @Override
    public Integer getTotalShares() throws RemoteException{
        return totalShares;
    }
    @Override
    public void uploadFile(byte[] mydata) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        file = new File(FILE_PATH + getJobOwner() + jobName + dtf.format(now));
        filePath = file.getAbsolutePath();

        //creates a file ||
        //               vv
        FileOutputStream out = new FileOutputStream(file);
        out.write(mydata);
        out.flush();
        out.close();
    }
    @Override
    public byte[] downloadFileFromServer(String serverpath) throws RemoteException,IOException {
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
    public String getWorkload() throws RemoteException {
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
