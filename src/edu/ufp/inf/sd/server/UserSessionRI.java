package edu.ufp.inf.sd.server;

import java.util.ArrayList;

public interface UserSessionRI {
    void logout();
    ArrayList<JobShopRI> getJobs(String username);
    ArrayList<WorkerRI> getWorkers(String username);
    void createJob(String username, String name);
    void pauseJob();
    void deleteJob();
    void joinJob(String username,WorkerRI worker);
    void createWorker(String username,WorkerRI worker);
    void deleteWorker(String username,String name,WorkerRI worker);
    boolean associateUserWorker(String username,WorkerRI worker);
}
