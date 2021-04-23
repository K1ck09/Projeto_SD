package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ItemController {
    public Label tableJob;
    public Label tableOwner;
    public Label tableStrat;
    public Label tableReward;
    public Label tableWorkers;
    public Label tableState;
    public Label tableWorkLoad;
    public Label tableSharesPerWorker;
    HashMap<String,String> item =new HashMap<>();
    private JobShopClient client;



    public void setClient(JobShopClient client) {
        this.client = client;
    }

    public void handlerInsideJob(ActionEvent actionEvent) throws IOException {
        insertItens();
        changeToMenuScene(actionEvent);
    }

    public void printHashMap(HashMap<String, String> hashMap) {
        for (String value : hashMap.values()) {
            System.out.println("value: " + value);
        }
    }

    private void changeToMenuScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/jobMenu.fxml"));
        Parent menuParent = loader.load();
        Scene menuScene = new Scene(menuParent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        JobController controller = loader.getController();
        JobGroupRI jobGroupRI = this.client.userSessionRI.getJobList().get(item.get("job"));
        controller.init(item,this.client,jobGroupRI.getJobName());
        app_stage.setScene(menuScene);
        app_stage.setHeight(668.0);
        app_stage.setWidth(1049.0);
        app_stage.show();
    }

    void insertItens(){
        item.put("job", tableJob.getText());
        item.put("owner",tableOwner.getText());
        item.put("strat",tableStrat.getText());
        item.put("reward", tableReward.getText());
        item.put("workers",tableWorkers.getText());
        item.put("State",tableState.getText());
        item.put("load",tableWorkLoad.getText());
        item.put("shares",tableSharesPerWorker.getText());
    }

    void clearItemMap(){
        item.clear();
    }
}
