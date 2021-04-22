package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI,Runnable {
    private String jobName;
    private String owner;
    private String strat;
    private String reward;
    private State state;
    private String workLoad;
    private String sharesPerWorker;
    private File file;
    Map<Integer, WorkerRI> jobWorkers = new HashMap<>();
    ArrayList<OperationsRI> jobOperations = new ArrayList<>();

    protected JobGroupImpl(String jobName,String owner,String strat,String reward,String workLoad,String sharesPerWorker,File file) throws RemoteException {
        this.jobName=jobName;
        this.owner=owner;
        this.strat=strat;
        this.reward=reward;
        this.state=new State();
        this.workLoad=workLoad;
        this.sharesPerWorker=sharesPerWorker;
        this.file=file;
    }

    @Override
    public void run() {
        //Criar Operation e enviar
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

    @Override
    public void attachWorker(WorkerRI worker) throws RemoteException {
        jobWorkers.put(worker.getId(),worker);
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

}
