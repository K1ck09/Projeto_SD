package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobShopRI;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {
    @FXML
    public VBox table;
    @FXML
    public Button btnCreateTask;
    @FXML
    public Label menuUsername;
    @FXML
    public Label menuCredits;
    @FXML
    public TextField createJobName;
    @FXML
    public TextField createJobReward;
    @FXML
    public ChoiceBox<String> createJobStrategy;
    private final ObservableList<String> stratTypes = FXCollections.observableArrayList("TabuSearch","Genetic Algorithm");

    private HashMap<String,String> item;
    public JobShopClient client;

    public void MenuControllerInit(JobShopClient client) throws RemoteException {
        this.client=client;
        menuUsername.setText("Username: "+client.userSessionRI.getUsername());
        menuCredits.setText("Credits: "+client.userSessionRI.getCredits());
        createJobStrategy.setItems(stratTypes);
        createJobStrategy.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(!item.containsKey(stratTypes.get(t1.intValue()))){
                    item.put("strat", stratTypes.get(t1.intValue()));
                }else if(item.containsKey(stratTypes.get(t1.intValue())) && item.get("strat").compareTo(stratTypes.get(t1.intValue()))!=0 ){
                    item.replace("strat", stratTypes.get(t1.intValue()));
                }

            }
        });

        // client.userSessionRI.setCredtis(10);
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws RemoteException {
        item.put("job",createJobName.getText());
        item.put("reward",createJobReward.getText());
        JobShopRI job= client.userSessionRI.createJob(client.userSessionRI.getUsername(),createJobName.getText());
        String jsspInstancePath = "edu/ufp/inf/sd/data/la01.txt";
        int makespan = job.runTS(jsspInstancePath);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                "[TS] Makespan for {0} = {1}",
                new Object[]{jsspInstancePath,String.valueOf(makespan)});

    }
}
