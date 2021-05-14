package edu.ufp.inf.sd.rmq.client;

import java.io.IOException;
import java.rmi.Remote;


public interface MenuControllerRI extends Remote {
        void updateMenu() throws IOException;
}
