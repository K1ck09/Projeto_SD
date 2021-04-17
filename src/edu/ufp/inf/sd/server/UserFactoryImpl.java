package edu.ufp.inf.sd.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserFactoryImpl extends UnicastRemoteObject implements UserFactoryRI {
    DBMockup db;
    HashMap<String, User> sessions;

    protected UserFactoryImpl() throws RemoteException {
        super();
        this.db = new DBMockup();
        this.sessions = new HashMap<String, User>();
    }

    @Override
    public boolean register(String username, String password) {
        if (!db.existsUser(username, password)) {
            db.registerUser(username, password);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Register user : {0}", new Object[]{username, password});
            return true;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Not Registered");
        return false;
    }

    @Override
    public UserSessionRI login(String username, String password) {
        User user = new User(username, password);
        if (db.existsUser(username, password)) {
            if (!this.sessions.containsKey(username)) {
                this.sessions.put(username, user);
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Sessions Sent");
            return new UserSessionImpl(this, user);
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-User not found");
        return null;
    }
}
