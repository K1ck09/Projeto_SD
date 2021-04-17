package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobShopRI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLOutput;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController  implements Initializable {
    public JobShopClient client;
    @FXML
    public Label accountUsername;
    @FXML
    public Button btnCreateTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
