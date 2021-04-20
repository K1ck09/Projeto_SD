package edu.ufp.inf.sd.server;

import java.io.Serializable;
import java.rmi.RemoteException;

public class State implements Serializable {
    private String currentState;

    public final String PAUSED = "Paused";
    public final String FINISHED = "Finished";
    public final String UNFINISHED = "Unfinished";
    public final String ONGOING = "Ongoing";

    public State(String currentState) {
        this.currentState=currentState;
    }

    public String getCurrentState() throws RemoteException {
        return currentState;
    }
}
