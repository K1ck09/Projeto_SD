package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.server.JobGroupRI;
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
    public Label tableBestResult;
    HashMap<String, String> item = new HashMap<>();
    private JobShopClient client;
    private JobGroupRI thisJob;

    /**
     * Initialize a client
     * @param client - JobShopClient
     */
    public void setClient(JobShopClient client) {
        this.client = client;
    }

    /**
     * Handles Job click and redirect to the Job Menu
     * @param actionEvent - An action event
     * @throws IOException
     */
    public void handlerInsideJob(ActionEvent actionEvent) throws IOException {
        this.thisJob = this.client.userSessionRI.getJobList().get(tableJob.getText());
        changeToMenuScene(actionEvent);
    }

    /**
     * Changes to the Job Menu Scene
     * @param actionEvent - An action event
     * @throws IOException
     */
    private void changeToMenuScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/jobMenu.fxml"));
        Parent menuParent = loader.load();
        Scene menuScene = new Scene(menuParent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        JobController controller = loader.getController();
        controller.init(this.client, thisJob);
        app_stage.setScene(menuScene);
        app_stage.setHeight(668.0);
        app_stage.setWidth(1049.0);
        app_stage.show();
    }
}
