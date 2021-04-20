package edu.ufp.inf.sd.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBMockup {

    private HashMap<String, User> users;// = new ArrayList();
    private HashMap<String, User> sessions;

    public DBMockup() {
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();




        User user = new User("a","a",10);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Register user : {0}", new Object[]{user.getCredits()});
        users.put(user.getUsername(),user);
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

    public void removeSession(String username){
        this.sessions.remove(username);
    }

}
