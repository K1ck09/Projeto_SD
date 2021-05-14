package edu.ufp.inf.sd.rmi.client;

import java.io.IOException;
import java.rmi.Remote;


public interface MenuControllerRI extends Remote {
        void updateMenu() throws IOException;
}
