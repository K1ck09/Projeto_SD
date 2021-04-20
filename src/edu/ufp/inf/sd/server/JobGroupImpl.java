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

    protected JobGroupImpl() throws RemoteException {
    }

    @Override
    public void run() {

    }
}
