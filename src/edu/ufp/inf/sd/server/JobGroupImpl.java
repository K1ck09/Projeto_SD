package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;
import edu.ufp.inf.sd.util.tabusearch.TabuSearchJSSP;

import java.awt.*;
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
    public void attachWorker(WorkerRI worker) throws RemoteException {
        //dar attach ao worker
        jobWorkers.put(worker.getId(),worker);
        //ver state e criar thread
        if(this.state.getCurrentState().compareTo("Available")==0){
            Operations op = new Operations(file,jobName,worker);
            Thread thread = new Thread(op);
            thread.start();
        }

    }

    @Override
    public String getState() throws RemoteException {
        return state.getCurrentState();
    }

    @Override
    public void setState(State state) {
        this.state = state;

    }

    @Override
    public void detachWorker(WorkerRI worker) throws RemoteException {
        jobWorkers.remove(worker.getId());
    }

    @Override
    public void uploadFile(byte[] mydata) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        file = new File(FILE_PATH + getJobOwner() + jobName + dtf.format(now));
        filePath = file.getAbsolutePath();

        //creates a file ||
        //               vv
       /* FileOutputStream out = new FileOutputStream(file);
        out.write(mydata);
        out.flush();
        out.close();*/
    }

    @Override
    public byte[] downloadFileFromServer(String serverpath) throws RemoteException {
        byte[] mydata;

        File serverpathfile = new File(serverpath);
        mydata = new byte[(int) serverpathfile.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(serverpathfile);
            try {
                in.read(mydata, 0, mydata.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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



    public void printHashMap(Map<Integer, WorkerRI> hashMap) throws RemoteException {
        Collection<WorkerRI> workers = hashMap.values();
        for (WorkerRI worker : workers) {
            System.out.println("value: " + worker.getId());
        }
    }

}
