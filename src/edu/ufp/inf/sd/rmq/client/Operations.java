package edu.ufp.inf.sd.rmq.client;

import edu.ufp.inf.sd.rmq.util.geneticalgorithm.CrossoverStrategies;
import edu.ufp.inf.sd.rmq.util.geneticalgorithm.GeneticAlgorithmJSSP;
import edu.ufp.inf.sd.rmq.util.tabusearch.TabuSearchJSSP;

import java.io.IOException;
import java.rmi.RemoteException;

public class Operations implements Runnable {
    private GeneticAlgorithmJSSP ga=null;
    private TabuSearchJSSP ts=null;
    private WorkerRI worker;

    public Operations(String filepath, WorkerImpl worker) {
        ts=new TabuSearchJSSP(filepath);
        this.worker=worker;
    }

    public Operations(String filepath, WorkerImpl worker,String queue, CrossoverStrategies crossoverStrategies){
        ga= new GeneticAlgorithmJSSP(filepath,queue,crossoverStrategies);
        this.worker=worker;
    }

    @Override
    public void run() {
        if(ts!=null){
            try {
                Thread.sleep(1000);
                worker.updateMakeSpan(ts.run());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }else if(ga!=null){
                ga.run();
                System.out.println("HEREEEE");
        }
    }
}
