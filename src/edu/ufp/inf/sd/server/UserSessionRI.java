package edu.ufp.inf.sd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserSessionRI extends Remote {
   /* void logout();
    ArrayList<JobShopRI> getJobs(String username);
    ArrayList<WorkerRI> getWorkers(String username);
    void createJob(String username, String name);
    void pauseJob();
    void deleteJob();
    void joinJob(String username,WorkerRI worker);
    void createWorker(String username,WorkerRI worker);
    void deleteWorker(String username,String name,WorkerRI worker);
    boolean associateUserWorker(String username,WorkerRI worker);*/
    public String getUsername() throws RemoteException;
}
