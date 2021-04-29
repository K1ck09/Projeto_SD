package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;
import edu.ufp.inf.sd.util.tabusearch.TabuSearchJSSP;

import java.io.File;
import java.rmi.RemoteException;

public class Operations implements Runnable {
    public File file;
    public String jobName;
    public int makespan;
    public TabuSearchJSSP ts;
    public WorkerRI worker;

    public Operations(File file, String jobName, WorkerRI worker) {
        this.file=file;
        this.jobName=jobName;
        // criar isntacia TSJSSP e enviar para o worker
        ts=new TabuSearchJSSP(file.getAbsolutePath()); // no worker
        this.worker=worker;
    }

    @Override
    public void run() {
        try {
            // enviar copia? ou referencia? desta Operation para o worker
            worker.setOperation(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
