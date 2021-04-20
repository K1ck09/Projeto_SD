package edu.ufp.inf.sd.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI,Runnable {
    private String jobName;
    private String owner;
    private String strat;
    private String reward;
    private State state;
    ArrayList<WorkerRI> jobWorkers = new ArrayList<>();
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
    public Integer getWorkersSize() {
        return jobWorkers.size();
    }
    @Override
    public String getState() throws RemoteException {
        return state.getCurrentState();
    }

}
