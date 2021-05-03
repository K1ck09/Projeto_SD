package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.util.tabusearch.TabuSearchJSSP;

import java.io.IOException;
import java.rmi.RemoteException;

public class Operations implements Runnable {
    private TabuSearchJSSP ts;
    private WorkerRI worker;

    public Operations(String filepath, WorkerImpl worker) {
        ts=new TabuSearchJSSP(filepath);
        this.worker=worker;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            worker.updateMakeSpan(ts.run());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
