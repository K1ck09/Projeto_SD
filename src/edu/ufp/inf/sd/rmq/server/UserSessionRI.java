package edu.ufp.inf.sd.rmq.server;

import edu.ufp.inf.sd.rmq.client.JobShopClient;
import edu.ufp.inf.sd.rmq.client.MenuController;
import edu.ufp.inf.sd.rmq.client.MenuControllerRI;
import edu.ufp.inf.sd.rmq.client.WorkerRI;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public interface UserSessionRI extends Remote {
    String getUsername() throws RemoteException;

    String getCredits() throws RemoteException;

    void setCredits(User user, Integer credits) throws RemoteException;

    Map<String, JobGroupRI> getJobList() throws RemoteException;

    void logout() throws RemoteException;

    boolean isJobUnique(String jobName) throws RemoteException;

    Map<Integer, WorkerRI> getWorkersMap(String jobGroupRI) throws RemoteException;

    Map<String, JobGroupRI> createJob(UserSessionRI userSessionRI, HashMap<String, String> item) throws IOException;

    User getUser() throws RemoteException;

    void updateMenus() throws IOException;

    boolean isError() throws RemoteException;

    void addList(MenuControllerRI controller,String user) throws IOException;

    void removeJob(String jobName) throws IOException;

    void removeFromList(String username)throws  RemoteException;
}
