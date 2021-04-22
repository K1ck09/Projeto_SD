package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.State;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WorkerImpl extends UnicastRemoteObject implements WorkerRI {
    Integer id;
    private JobShopClient client;
    private State state;
    private String owner;
    private String jobGroupName;
    private int currentShares;
    private int totalShares;
    private int currentMakespan;
    private ArrayList<Thread> threads = new ArrayList<>();

    protected WorkerImpl(String jobOwner) throws RemoteException {
        this.owner=jobOwner;
    }

    @Override
    public void setOperation() throws RemoteException {
        //Precisa de receber operation
    }

    @Override
    public void resumeWorker() throws RemoteException{

    }

    @Override
    public void pauseWorker() throws RemoteException{

    }

    @Override
    public void deleteWorker() throws RemoteException{

    }

    public Integer getId() throws RemoteException{
        return id;
    }
}
