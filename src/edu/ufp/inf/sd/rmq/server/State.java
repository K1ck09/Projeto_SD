package edu.ufp.inf.sd.rmq.server;

import java.io.Serializable;
import java.rmi.RemoteException;

public class State implements Serializable {
    private String currentState;
    private final String id;


    public State(String currentState, String id) {
        this.currentState=currentState;
        this.id=id;
    }

    public String getCurrentState() throws RemoteException { return currentState; }

    public void setCurrentState(String currentState) { this.currentState = currentState; }

    public String getId() {
        return id;
    }
}
