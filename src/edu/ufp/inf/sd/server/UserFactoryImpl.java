package edu.ufp.inf.sd.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserFactoryImpl extends UnicastRemoteObject implements UserFactoryRI {
    DBMockup db;

    protected UserFactoryImpl() throws RemoteException {
        super();
        this.db = new DBMockup();
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        if (!db.existsUser(username, password)) {
            db.registerUser(username, password);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Register user : {0}", new Object[]{username, password});
            return true;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Not Registered");
        return false;
    }

    @Override
    public UserSessionRI login(String username, String password) throws RemoteException{
        if (db.existsUser(username, password)) {
            User user=db.getUser(username);
            if (!db.getSessions().containsKey(username)) {
                this.db.addSessions(user);
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Sessions Sent");
            return new UserSessionImpl(this.db, user);
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-User not found");
        return null;
    }
}
