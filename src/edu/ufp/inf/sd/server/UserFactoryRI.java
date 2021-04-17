package edu.ufp.inf.sd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
<<<<<<< HEAD

public interface UserFactoryRI extends Remote {
    boolean register (String username,String password) throws RemoteException;
    UserSessionRI login (String username,String password) throws RemoteException;
=======
>>>>>>> parent of 2dfdddf (.)

public interface UserFactoryRI extends Remote {
    boolean register (String username,String password)throws RemoteException;
    UserSessionRI login (String username,String password)throws RemoteException;
}
