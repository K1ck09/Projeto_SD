package edu.ufp.inf.sd.server;

import java.util.ArrayList;

public class DBMockup {

    private final ArrayList<User> users;// = new ArrayList();

    public DBMockup() {
        users = new ArrayList<>();
        User user = new User("a","a");
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
}
