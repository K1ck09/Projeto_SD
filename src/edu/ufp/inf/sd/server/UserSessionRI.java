package edu.ufp.inf.sd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserSessionRI extends Remote {
   void createJob(String username, String name) throws RemoteException;
   String getUsername() throws RemoteException;
   String getCredits() throws RemoteException;
    void setCredtis(int credits)throws RemoteException;

    void logout() throws RemoteException;
}
