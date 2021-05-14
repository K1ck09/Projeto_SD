package edu.ufp.inf.sd.rmq.client;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JobControllerRI extends Remote {
     void updateGUI() throws IOException;
}
