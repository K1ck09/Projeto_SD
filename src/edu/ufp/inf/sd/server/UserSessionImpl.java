package edu.ufp.inf.sd.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI {
    DBMockup db;
    User user;

    public UserSessionImpl(DBMockup db, User user) throws RemoteException {
        super();
        this.user=user;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Logged user : {0}", new Object[]{user.getUsername()});
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
    public HashMap<String, JobGroupRI> getJobList() throws RemoteException {
        return db.getJobGroups();
    }

    @Override
    public void logout() throws RemoteException {
        this.db.removeSession(this.user.getUsername());
    }

    @Override
    public HashMap<String, JobGroupRI> createJob(HashMap<String,String> item) throws RemoteException {
        JobGroupRI jobGroup= new JobGroupImpl(item.get("job"),item.get("owner"),item.get("strat"),item.get("reward"));
        db.addJob(jobGroup);
        return db.getJobGroups();
    }

    public JobGroupRI getJobGroup(String jobName){
        return db.getJobGroups().get(jobName);
    }


}
