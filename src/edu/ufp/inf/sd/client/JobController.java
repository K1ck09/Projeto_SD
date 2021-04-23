package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
import edu.ufp.inf.sd.server.State;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class JobController {

    public Label jobName;
    public Label jobOwner;
    public Label jobStrat;
    public Label jobReward;
    public Label jobWorkers;
    public Label jobState;
    public ScrollPane tableWorkers;
    public Button btnAddWorkers;
    public TextField workersNum;
    public Label menuUsername;
    public Label menuCredits;
    public Button btnWorkerPause;
    public Button btnWorkerResume;
    public Button btnWorkerDelete;
    public Button btnJobPause;
    public Button btnJobResume;
    public Button btnJobDelete;
    public VBox table;
    public Label jobWorkload;
    public Label jobSharesPerWorker;
    public Label menuJobLabel;
    public Label menuWorkerLabel;
    public VBox btnsJob;
    private JobShopClient client;
    private JobGroupRI jobGroupRI;
    private Map<Integer, WorkerRI> workersMap = new HashMap<>();

    public void init(HashMap<String, String> item, JobShopClient client, JobGroupRI jobGroupRI) throws RemoteException {
        jobName.setText(item.get("job"));
        jobOwner.setText(item.get("owner"));
        jobStrat.setText(item.get("strat"));
        jobReward.setText(item.get("reward"));
        jobWorkers.setText(item.get("workers"));
        jobState.setText(item.get("State"));
        jobWorkload.setText(item.get("load"));
        jobSharesPerWorker.setText(item.get("shares"));
        this.client = client;
        this.jobGroupRI = jobGroupRI;
        if (this.client.userSessionRI.getUsername().compareTo(jobGroupRI.getJobOwner()) == 0) {
            btnsJob.setVisible(true);
        }
        menuCredits.setText(this.client.userSessionRI.getCredits());
        menuUsername.setText(this.client.userSessionRI.getUsername());
        workersMap = client.userSessionRI.getWorkersMap(jobGroupRI);
        if (!workersMap.isEmpty()) {
            insertWorkersInTable();
        }
    }

    private void insertWorkersInTable() {

    }

    public void handlerExit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void handlerAttachWorkers(ActionEvent actionEvent) throws RemoteException {
        int num = Integer.parseInt(workersNum.getText());
        for (int i = 0; i < 1; i++) {
            WorkerRI worker = new WorkerImpl(client,client.userSessionRI.getUserWorkersSize()+1, jobGroupRI.getJobOwner(),new State(),jobGroupRI.getJobName());
            jobGroupRI.attachWorker(worker);
        }
        // é rpeciso dar sinal ao job que já tem workers
    }

    public void handlerPauseWorker(ActionEvent actionEvent) {
    }

    public void handlerResumeWorker(ActionEvent actionEvent) {
    }

    public void handlerDeleteWorker(ActionEvent actionEvent) {
    }

    public void handlerPauseJob(ActionEvent actionEvent) {
    }

    public void handlerResumeJob(ActionEvent actionEvent) {
    }

    public void handlerDeleteJob(ActionEvent actionEvent) {
    }

    public void handlerMenuHome(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/Menu.fxml"));
        Parent menuParent = loader.load();
        Scene menuScene = new Scene(menuParent);
        Stage app_stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        MenuController controller = loader.getController();
        controller.MenuControllerInit(client);
        app_stage.setScene(menuScene);
        app_stage.setHeight(668.0);
        app_stage.setWidth(1049.0);
        app_stage.show();
    }

    public void showWorkerbuttons(int workerID) {

    }

    public void showJobButtons() {

    }
}

