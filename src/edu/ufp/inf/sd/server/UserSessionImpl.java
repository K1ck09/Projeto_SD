package edu.ufp.inf.sd.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI {
    UserFactoryImpl userFactory;
    User user;

    public UserSessionImpl(UserFactoryImpl userFactory, User user) throws RemoteException {
        super();
        this.user=user;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\n-Logged user : {0}", new Object[]{user.getUsername()});
        this.userFactory=userFactory;
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
    public void logout() throws RemoteException {
        this.userFactory.remove(this.user.getUsername());
    }

    @Override
    public void createJob(String username, String name) throws RemoteException {

    }
}
