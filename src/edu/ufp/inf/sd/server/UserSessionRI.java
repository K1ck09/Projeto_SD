package edu.ufp.inf.sd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserSessionRI extends Remote {
   /* void logout();
    ArrayList<JobShopRI> getJobs(String username);
    ArrayList<WorkerRI> getWorkers(String username);
    void pauseJob();
    void deleteJob();
    void joinJob(String username,WorkerRI worker);
    void createWorker(String username,WorkerRI worker);
    void deleteWorker(String username,String name,WorkerRI worker);
    boolean associateUserWorker(String username,WorkerRI worker);*/
   JobShopRI createJob(String username, String name) throws RemoteException;
   String getUsername() throws RemoteException;
   String getCredits() throws RemoteException;
    void setCredtis(int credits)throws RemoteException;
}
