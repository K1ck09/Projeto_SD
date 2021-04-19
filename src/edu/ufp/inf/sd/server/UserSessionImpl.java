package edu.ufp.inf.sd.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI {
    UserFactoryImpl userFactory;
    User user;
    DBMockup db;
    public UserSessionImpl(UserFactoryImpl userFactory, User user, DBMockup db) throws RemoteException {
        super();
        this.user=user;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Register user : {0}", new Object[]{user.getCredits()});
        this.userFactory=userFactory;
        this.db=db;
    }

    public String getUsername() throws RemoteException{
        return user.getUsername();
    }

    @Override
    public String getCredits() throws RemoteException {
        return String.valueOf(user.getCredits());
    }

    @Override
    public void setCredtis(int credits) throws RemoteException{
        user.setCredits(credits);

    }

    @Override
    public void logout() {

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
