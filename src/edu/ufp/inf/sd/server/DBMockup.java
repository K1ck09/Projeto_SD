package edu.ufp.inf.sd.server;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBMockup {

    private final ArrayList<User> users;// = new ArrayList();

    public DBMockup() {
        users = new ArrayList<>();
        User user = new User("a","a",10);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Register user : {0}", new Object[]{user.getCredits()});
        users.add(user);
    }

    public boolean existsUser(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUsername().compareTo(u) == 0 && usr.getPassword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
    }

    public void registerUser(String u, String p) {
        if (!existsUser(u, p)) {
            users.add(new User(u, p));
        }
    }

    public User getUser(String username) {
        for (User usr : this.users) {
            if (usr.getUsername().compareTo(username) == 0) {
                return usr;
            }
        }
        return null;
    }
}
