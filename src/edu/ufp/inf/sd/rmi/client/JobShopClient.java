package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.server.UserFactoryRI;
import edu.ufp.inf.sd.rmi.server.UserSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * <p>
 * Title: Projecto SD</p>
 * <p>
 * Description: Projecto apoio aulas SD</p>
 * <p>
 * Copyright: Copyright (c) 2017</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Rui S. Moreira
 * @version 3.0
 */
public class JobShopClient {


    /**
     * Context for connecting a RMI client MAIL_TO_ADDR a RMI Servant
     */
    private transient SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */

    public UserFactoryRI userFactoryRI;
    public UserSessionRI userSessionRI;

    /**
     * Starts JobShopClient service
     */
    public JobShopClient() {
        try {
            //List ans set args
            //System.out.println(System.setProperty("java.rmi.server.hostname","172.19.224.1"));
            String registryIP = "localhost";
            String registryPort = "4010";
            String serviceName = "JobShopService";
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(JobShopClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Starts the lookupService
     * @return an UserFactoryRI
     */
    public Remote lookupService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);
                
                //============ Get proxy MAIL_TO_ADDR HelloWorld service ============
                userFactoryRI = (UserFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return userFactoryRI;
    }
}
