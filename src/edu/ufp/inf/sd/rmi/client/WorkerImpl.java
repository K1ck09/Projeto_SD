package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.server.JobGroupRI;
import edu.ufp.inf.sd.rmi.server.State;
import edu.ufp.inf.sd.rmi.server.User;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WorkerImpl extends UnicastRemoteObject implements WorkerRI {
    Integer id;
    private final JobShopClient client;
    private State state;
    private final User owner;
    private final String jobGroupName;
    private int bestMakespan= Integer.MAX_VALUE;
    private int totalShares=0;
    private int currentMakespan;
    private int totalRewarded=0;
    private final JobGroupRI JobGroupRI;
    private static final String PATH_FILE="C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\rmi\\client\\temp\\";
    private File file;


    protected WorkerImpl( JobShopClient client,Integer id,User jobOwner, State state,String jobGroupName) throws RemoteException {
        this.id=id;
        this.owner=jobOwner;
        this.client=client;
        this.state=state;
        this.jobGroupName = jobGroupName;
        this.JobGroupRI=client.userSessionRI.getJobList().get(jobGroupName);
    }

    @Override
    public void setOperation() {
        Operations op = new Operations(file.getAbsolutePath(), this);
        Thread t=new Thread(op);
        t.start();
    }

    @Override
    public void changeState(String state) {
        this.state.setCurrentState(state);
    }

    @Override
    public void setState(String state) throws IOException {
        this.state.setCurrentState(state);
        JobGroupRI.updateTotalShares(this);
    }

    @Override
    public synchronized void updateMakeSpan(int makespan) throws IOException {
            this.currentMakespan=makespan;
            //System.out.println("["+id+"] -> "+currentMakespan);
            if(this.bestMakespan>this.currentMakespan){
                this. bestMakespan=this.currentMakespan;
            }
            JobGroupRI.updateTotalShares(this);
    }

    @Override
    public void setFile(String filePath)throws IOException {
        downloadFile(filePath);
        JobGroupRI.updateTotalShares(this);
    }

    private void downloadFile( String filepath) throws IOException {
        byte [] data = JobGroupRI.downloadFileFromServer(filepath);
        this.file=new File(PATH_FILE+this.id+"_"+owner.getUsername()+"_"+jobGroupName);
        FileOutputStream out = new FileOutputStream(this.file);
        out.write(data);
        out.flush();
        out.close();
    }

    public int getCurrentMakespan() {
        return currentMakespan;
    }
    public int getBestMakespan() {
        return bestMakespan;
    }
    public int getTotalShares() {
        return totalShares;
    }
    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public State getState() {
        return state;
    }
    @Override
    public User getOwner() {
        return owner;
    }
    @Override
    public String getJobGroupName() {
        return jobGroupName;
    }
    @Override
    public JobShopClient getClient() {
        return client;
    }

    @Override
    public int getTotalRewarded() {
        return totalRewarded;
    }

    public void setTotalRewarded(int totalRewarded) {
        this.totalRewarded =+ totalRewarded;
    }
}
