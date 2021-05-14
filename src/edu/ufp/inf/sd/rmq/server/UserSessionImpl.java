package edu.ufp.inf.sd.rmq.server;

import edu.ufp.inf.sd.rmq.client.MenuController;
import edu.ufp.inf.sd.rmq.client.MenuControllerRI;
import edu.ufp.inf.sd.rmq.client.WorkerRI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI {
    DBMockup db;
    User user;
    private boolean error=false;

    public UserSessionImpl(DBMockup db, User user) throws RemoteException {
        super();
        this.user=user;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Logged user : {0}", new Object[]{user.getUsername()});
        this.db=db;
    }

    public UserSessionImpl(boolean error) throws RemoteException {
        super();
        this.error=error;
    }

    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getCredits() {
        user = db.getUser(user.getUsername());
        return String.valueOf(user.getCredits());
    }

    @Override
    public void setCredits(User user,int credits) {
        user.addCredits(credits);
        db.updateUser(user);
    }

    @Override
    public Map<String, JobGroupRI> getJobList() {
        return db.getJobGroups();
    }

    @Override
    public void logout() throws RemoteException {
        this.db.removeSession(this.user.getUsername());
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Logged OUT : {0}", new Object[]{user.getUsername()});
        this.user=null;
    }

    @Override
    public boolean isJobUnique(String jobName) throws RemoteException {
        return db.getJobGroups().containsKey(jobName);
    }

    @Override
    public Map<Integer, WorkerRI> getWorkersMap(String jobGroupRI) throws RemoteException {
        return db.getJobGroups().get(jobGroupRI).getJobWorkers();
    }


    @Override
    public Map<String, JobGroupRI> createJob( UserSessionRI userSessionRI, HashMap<String, String> item) throws IOException {
        JobGroupRI jobGroup= new JobGroupImpl(userSessionRI,item.get("job"),item.get("owner"),item.get("strat"),item.get("reward"),item.get("load"));
        db.addJob(jobGroup);
        updateMenus();
        return db.getJobGroups();
    }
    @Override
    public void updateMenus() throws IOException {
        for(MenuControllerRI c : db.getMenuList().values()){
            c.updateMenu();
        }
    }

    public JobGroupRI getJobGroup(String jobName){
        return db.getJobGroups().get(jobName);
    }

    public User getUser()throws RemoteException {
        return user;
    }

    @Override
    public void addList(MenuControllerRI controller,String user) throws IOException {
        this.db.addList(controller,user);
    }

    @Override
    public void removeFromList(String username) throws RemoteException {
        this.db.removeFromList(username);
    }

    @Override
    public void removeJob(String jobName) throws IOException {
        this.db.removeJob(jobName);
        updateMenus();
    }

    public boolean isError() throws RemoteException{
        return error;
    }
}
