package edu.ufp.inf.sd.client;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handlerTabAccount(Event event) throws RemoteException {
        if(client.userSessionRI.getUsername()!=null){
            accountUsername.setText(client.userSessionRI.getUsername());
        }
    }
}
