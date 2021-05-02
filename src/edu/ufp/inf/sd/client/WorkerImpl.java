package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
import edu.ufp.inf.sd.server.JobThread;
import edu.ufp.inf.sd.server.State;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class WorkerImpl extends UnicastRemoteObject implements WorkerRI {
    Integer id;
    private final JobShopClient client;
    private State state;
    private final String owner;
    private final String jobGroupName;
    private int bestMakespan= Integer.MAX_VALUE;
    private int totalShares=0;
    private int currentMakespan;
    private int totalRewarded=0;
    private JobGroupRI JobGroupRI;
    private Operations op;
    private static final String PATH_FILE="C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\client\\temp\\";
    private File file;
    private JobController controller;
    private JobThread jobThread;

    protected WorkerImpl( JobShopClient client,Integer id,String jobOwner, State state,String jobGroupName,JobController controller) throws RemoteException {
        this.id=id;
        this.owner=jobOwner;
        this.client=client;
        this.state=state;
        this.jobGroupName = jobGroupName;
        this.JobGroupRI=client.userSessionRI.getJobList().get(jobGroupName);
        this.controller=controller;
    }

    @Override
    public void setOperation(String filepath, JobThread jobThread) throws RemoteException,IOException {
        this.jobThread=jobThread;
        // Usar quando implemntar JobThread
    }

    @Override
    public void setOperation(String filePath)throws RemoteException,IOException {
        downloadFile(filePath);
        op= new Operations(file.getAbsolutePath(),this);
        this.state.setCurrentState("Ongoing");
        Thread t=new Thread(op);
        t.start();
    }

    @Override
    public synchronized void updateMakeSpan(int makespan) throws RemoteException,IOException {
            this.currentMakespan=makespan;
            System.out.println("["+id+"] -> "+currentMakespan);
            if(this.bestMakespan>this.currentMakespan){
                this. bestMakespan=this.currentMakespan;
            }
            if(this.totalShares<Integer.parseInt(this.JobGroupRI.getSharesPerWorker())){
                controller.update();
                this.totalShares++;
                this.state.setCurrentState("StandBy");
                JobGroupRI.updateTotalShares(totalShares,this);
                Thread t=new Thread(op);
                t.start();
            }else{
                this.state.setCurrentState("Completed");
                //finish Share - Call method in Job
            }
    }


    private void downloadFile( String filepath) throws RemoteException,IOException {
        byte [] data = JobGroupRI.downloadFileFromServer(filepath);
        this.file=new File(PATH_FILE+this.id+"_"+owner+"_"+jobGroupName);
        FileOutputStream out = new FileOutputStream(this.file);
        out.write(data);
        out.flush();
        out.close();
    }

    public int getCurrentMakespan()throws RemoteException {
        return currentMakespan;
    }

    public int getBestMakespan() throws RemoteException{
        return bestMakespan;
    }

    public int getTotalShares() throws RemoteException{
        return totalShares;
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
    @Override
    public Integer getId() throws RemoteException{
        return id;
    }
    @Override
    public State getState() throws RemoteException{
        return state;
    }

    @Override
    public String getOwner() {
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


}
