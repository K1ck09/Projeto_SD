package edu.ufp.inf.sd.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI {
    UserFactoryImpl userFactory;
    User user;
    public UserSessionImpl(UserFactoryImpl userFactory, User user) throws RemoteException {
        super();
        this.user=user;
        this.userFactory=userFactory;
    }

    public String getUsername() throws RemoteException{
        return user.getUsername();
    }

    @Override
    public JobShopRI createJob(String username, String name) throws RemoteException {
        return new JobShopImpl();
    }

    /*@Override
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
    }*/
}
