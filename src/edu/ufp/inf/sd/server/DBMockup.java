package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.MenuController;
import edu.ufp.inf.sd.client.MenuControllerRI;
import edu.ufp.inf.sd.client.WorkerRI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBMockup {

    private HashMap<String, User> users;// = new ArrayList();
    private HashMap<String, User> sessions;

    //job name and job object
    private Map<String, JobGroupRI> jobGroups;
    private HashMap<String,MenuControllerRI> menuList;

    public DBMockup() {
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
        this.jobGroups =new HashMap<>();
        this.menuList = new HashMap<>();

        User user = new User("a","a",10000);
        User user1 = new User("q","q",10000);
        users.put(user.getUsername(),user);
        users.put(user1.getUsername(),user1);
    }
    // USERS METHODS
    public boolean existsUser(String u, String p) {
        return users.containsKey(u) && users.get(u).getPassword().compareTo(p)==0;
    }

    public void registerUser(String u, String p) {
        if (!existsUser(u, p)) {
            users.put(u,new User(u, p));
        }
    }

    public void updateUser(User user){
        this.users.replace(user.getUsername(),user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    // SESSIONS METHODS
    public HashMap<String, User> getSessions() {
        return sessions;
    }

    public void addSessions(User user) {
        this.sessions.put(user.getUsername(),user);
    }

    public void removeSession(String username) throws RemoteException {
        this.sessions.remove(username);
    }
    // JOB METHODS

    public Map<String, JobGroupRI> getJobGroups() {
        return jobGroups;
    }


    public void addJob(JobGroupRI jobGroup) throws RemoteException {
        this.jobGroups.put(jobGroup.getJobName(),jobGroup);
    }

    public void addList(MenuControllerRI controller,String user) {
        menuList.put(user,controller);
    }

    public HashMap<String,MenuControllerRI> getMenuList() {
        return menuList;
    }

    public void removeJob(String jobName) {
        this.jobGroups.remove(jobName);
    }

    public void removeFromList(String username) {
        this.menuList.remove(username);
    }
}
