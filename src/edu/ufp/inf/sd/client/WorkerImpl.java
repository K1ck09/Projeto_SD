package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.Operations;
import edu.ufp.inf.sd.server.State;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    protected WorkerImpl( JobShopClient client,Integer id,String jobOwner, State state,String jobGroupName) throws RemoteException {
        this.owner=jobOwner;
        this.client=client;
        this.state=state;
        this.jobGroupName=jobGroupName;
    }

    @Override
    public void setOperation(Operations operation) throws RemoteException {

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

    public State getState() throws RemoteException{
        return state;
    }


}
