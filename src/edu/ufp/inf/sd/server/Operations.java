package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.util.tabusearch.TabuSearchJSSP;

import java.io.File;

public class Operations {
    File file;
    String jobName;
    int makespan;
    TabuSearchJSSP ts;

    public Operations(File file, String jobName, TabuSearchJSSP ts) {
        this.file=file;
        this.jobName=jobName;
        this.ts=ts;
    }
}
