package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
import edu.ufp.inf.sd.server.State;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
    private ArrayList<Thread> threads = new ArrayList<>();
    private static final String PATH_FILE="C:\\Users\\danie\\Documents\\GitHub\\Projeto_SD\\src\\edu\\ufp\\inf\\sd\\client\\temp\\";
    private File file;

    protected WorkerImpl( JobShopClient client,Integer id,String jobOwner, State state,String jobGroupName) throws RemoteException {
        this.id=id;
        this.owner=jobOwner;
        this.client=client;
        this.state=state;
        this.jobGroupName = jobGroupName;
    }

    @Override
    public void setOperation(JobGroupRI job,String filepath) throws RemoteException {
        downloadFile(job,filepath);
        Operations op= new Operations(filepath,this);
        //criar threads - usar thread pool? ou criar threads a medida que é chamado?
        //verificar se já há ciclo na criação

    }

    @Override
    public void updateMakeSpan(int makespan) throws RemoteException {
        this.currentMakespan=makespan;
        if(this.bestMakespan>this.currentMakespan){
            this. bestMakespan=this.currentMakespan;
        }
        if(this.totalShares<Integer.parseInt(this.JobGroupRI.getSharesPerWorker())){
            this.totalShares++;
        }else{
            //finish Share - Call method in Job
        }
    }


    private void downloadFile(JobGroupRI job, String filepath) throws RemoteException {
        byte [] data = job.downloadFileFromServer(filepath);
        this.file=new File(PATH_FILE);
        FileOutputStream out= null;
        try {
            out = new FileOutputStream(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
