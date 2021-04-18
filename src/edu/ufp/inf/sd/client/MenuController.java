package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobShopRI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {
    public JobShopClient client;
    @FXML
    public Button btnCreateTask;
    @FXML
    public Label menuUsername;
    @FXML
    public Label menuCredits;

    public void MenuControllerInit(JobShopClient client) throws RemoteException {
            this.client=client;
            menuUsername.setText("Username: "+client.userSessionRI.getUsername());
            menuCredits.setText("Credits: "+client.userSessionRI.getCredits());
            client.userSessionRI.setCredtis(10);
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws RemoteException {
        JobShopRI job= client.userSessionRI.createJob(client.userSessionRI.getUsername(),"first");
        String jsspInstancePath = "edu/ufp/inf/sd/data/la01.txt";
        int makespan = job.runTS(jsspInstancePath);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "[TS] Makespan for {0} = {1}",
                new Object[]{jsspInstancePath,String.valueOf(makespan)});
    }
}
