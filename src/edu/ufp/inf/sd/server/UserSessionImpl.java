package edu.ufp.inf.sd.server;

import java.util.ArrayList;

public class UserSessionImpl implements UserSessionRI {
    public UserSessionImpl(UserFactoryImpl userFactory, User user) {
    }

    @Override
    public void logout() {

    }

    @Override
    public ArrayList<JobShopRI> getJobs(String username) {
        return null;
    }

    @Override
    public ArrayList<WorkerRI> getWorkers(String username) {
        return null;
    }

    @Override
    public void createJob(String username, String name) {

    }

    @Override
    public void pauseJob() {

    }

    @Override
    public void deleteJob() {

    }

    @Override
    public void joinJob(String username, WorkerRI worker) {

    }

    @Override
    public void createWorker(String username, WorkerRI worker) {

    }

    @Override
    public void deleteWorker(String username, String name, WorkerRI worker) {

    }

    @Override
    public boolean associateUserWorker(String username, WorkerRI worker) {
        return false;
    }
}
