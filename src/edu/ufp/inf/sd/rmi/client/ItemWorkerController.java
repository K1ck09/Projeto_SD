package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.server.JobGroupRI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class ItemWorkerController {

    public Label tableWorkerOwner;
    public Label tableWorkerState;
    public Label tableBestMakeSpan;
    public Label tableLastMakeSpan;
    public Label tableTimesSubmitted;
    public Label tableRewarded;
    public Label tableWorkerID;
    private JobController jobController;

    /**
     * Initialize a JobController
     * @param jobController - JobController
     */
    public void setJobController(JobController jobController){
        this.jobController=jobController;
    }

    /**
     * Gets worker ID and calls ShowWorkerButtons
     * @param actionEvent - An action event
     * @throws RemoteException
     */
    public void handlerWorkerSelect(ActionEvent actionEvent) throws RemoteException {
        Integer workerID = Integer.parseInt(tableWorkerID.getText());
        jobController.showWorkerbuttons(workerID);
     }


}
