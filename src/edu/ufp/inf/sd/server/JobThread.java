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
        Collection<WorkerRI> workers= null;
        try {
            workers = this.getJobWorkers().values();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(jobWorkers.values());
        for(WorkerRI w : workers){
            try {
                System.out.println(w.getOwner()+"-"+w.getId());
                if(w.getState().getCurrentState().compareTo("Available")==0){
                    w.setOperation(this.getFilePath(),this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void changeWorkerState(int bestMakeSpan){

    }

    void notifyAllWorkers(){

    }

    public void updateTotalShares() throws RemoteException {
        int newTotal= this.getTotalShares()+1;
        this.setTotalShares(newTotal);
    }
}
