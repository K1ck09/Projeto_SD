package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;

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
    Map<String, WorkerRI> jobWorkers = new HashMap<>();
    ArrayList<OperationsRI> jobOperations = new ArrayList<>();

    protected JobGroupImpl(String jobName,String owner,String strat,String reward) throws RemoteException {
        this.jobName=jobName;
        this.owner=owner;
        this.strat=strat;
        this.reward=reward;
        this.state=new State("Ongoing");
    }

    @Override
    public void run() {

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
    public Map<String, WorkerRI> getJobWorkers() throws RemoteException {
        return jobWorkers;
    }


    @Override
    public Integer getWorkersSize() {
        return jobWorkers.size();
    }

}
