package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.Operations;
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
    private String sharesPerWorker;
    private File file;
    private String filePath;
    Map<Integer, WorkerRI> jobWorkers = new HashMap<>();
    ArrayList<Operations> jobOperations = new ArrayList<>();

    private static final String FILE_PATH = "C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\server\\files\\";

    protected JobGroupImpl(String jobName, String owner, String strat, String reward, String workLoad, String sharesPerWorker) throws RemoteException {
        this.jobName = jobName;
        this.owner = owner;
        this.strat = strat;
        this.reward = reward;
        this.state = new State();
        this.workLoad = workLoad;
        this.sharesPerWorker = sharesPerWorker;
    }


    @Override
    public void attachWorker(WorkerRI worker) throws RemoteException,IOException {
        jobWorkers.put(worker.getId(),worker);
        if(this.state.getCurrentState().compareTo("Available")==0 || this.state.getCurrentState().compareTo("Ongoing")==0 ){
            worker.setOperation(this,filePath);
        }
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
    public String getSharesPerWorker() throws RemoteException {
        return sharesPerWorker;
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
