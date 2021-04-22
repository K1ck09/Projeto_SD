package edu.ufp.inf.sd.server;

import java.io.Serializable;
import java.rmi.RemoteException;

public class State implements Serializable {
    private String currentState;


    public State() {
        this.currentState="Available";
    }

    public String getCurrentState() throws RemoteException {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}
