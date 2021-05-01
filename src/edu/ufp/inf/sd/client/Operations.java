package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.util.tabusearch.TabuSearchJSSP;

import java.io.File;
import java.rmi.RemoteException;

public class Operations implements Runnable {
    TabuSearchJSSP ts;
    WorkerRI worker;

    public Operations(String filepath, WorkerImpl worker) {
        ts=new TabuSearchJSSP(filepath);
        this.worker=worker;
    }

    @Override
    public void run() {
        try {
            worker.updateMakeSpan(ts.run());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
