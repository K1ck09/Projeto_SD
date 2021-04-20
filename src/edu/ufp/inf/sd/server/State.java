package edu.ufp.inf.sd.server;

import java.io.Serializable;

public class State implements Serializable {
    public final String PAUSED = "Paused";
    public final String FINISHED = "Finished";
    public final String UNFINISHED = "Unfinished";
    public final String ONGOING = "Ongoing";

}
