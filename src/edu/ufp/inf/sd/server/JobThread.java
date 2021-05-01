package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;

public class JobThread extends JobGroupImpl implements Runnable {

    protected JobThread() throws RemoteException {
        super();
    }

    @Override
    public void run() {
        Collection<WorkerRI> workers= jobWorkers.values();
        for(WorkerRI w : workers){
            try {
                if(w.getState().getCurrentState().compareTo("Available")==0){
                    w.setOperation(this.getFilePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
