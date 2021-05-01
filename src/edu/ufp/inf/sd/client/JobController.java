package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
import edu.ufp.inf.sd.server.State;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Collection;
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
    public Label infoMessage;
    public VBox btnsWorkers;
    private JobShopClient client;
    private JobGroupRI jobGroupRI;
    private Map<Integer, WorkerRI> workersMap = new HashMap<>();

    public void init( JobShopClient client, JobGroupRI jobGroupRI) throws IOException {
        jobName.setText(jobGroupRI.getJobName());
        jobOwner.setText(jobGroupRI.getJobOwner());
        jobStrat.setText(jobGroupRI.getJobStrat());
        jobReward.setText(jobGroupRI.getJobReward());
        jobWorkers.setText(String.valueOf(jobGroupRI.getWorkersSize()));
        jobState.setText(jobGroupRI.getState());
        jobWorkload.setText(jobGroupRI.getWorkload());
        jobSharesPerWorker.setText(jobGroupRI.getSharesPerWorker());
        this.client = client;
        this.jobGroupRI = jobGroupRI;
        if (this.client.userSessionRI.getUsername().compareTo(jobGroupRI.getJobOwner()) == 0) {
            btnsJob.setVisible(true);
        }
        menuCredits.setText(this.client.userSessionRI.getCredits());
        menuUsername.setText(this.client.userSessionRI.getUsername());
        workersMap = jobGroupRI.getJobWorkers();
        if (!workersMap.isEmpty()) {
            insertWorkersInTable();
        }
    }

    private void insertWorkersInTable() throws IOException {
        table.getChildren().clear();
        printHashMap(workersMap);
        Collection<WorkerRI> workerList = workersMap.values();
        for(WorkerRI worker:workerList){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/tableWorker.fxml"));
            Parent menuParent = loader.load();
            ItemWorkerController controller = loader.getController();
            controller.setJobController(this);
            Node node = loader.getRoot();
            if (node instanceof AnchorPane) {
                AnchorPane anchor = (AnchorPane) node;
                ObservableList<Node> anchorIn = anchor.getChildren();
                for (Node anchorNode : anchorIn)
                    if (anchorNode instanceof HBox) {
                        HBox hbox = (HBox) anchorNode;
                        ObservableList<Node> nodeIn = hbox.getChildren();
                        for (Node label : nodeIn)
                            if (label instanceof Label) {
                                String id = label.getId();
                                if (id != null && id.compareTo("tableWorkerID") == 0) {
                                    ((Label) label).setText(String.valueOf(worker.getId()));
                                } else if (id != null && id.compareTo("tableWorkerState") == 0) {
                                    ((Label) label).setText(worker.getState().getCurrentState());
                                } else if (id != null && id.compareTo("tableWorkerOwner") == 0) {
                                    ((Label) label).setText(worker.getOwner());
                                }else if (id != null && id.compareTo("tableRewarded") == 0) {
                                    ((Label) label).setText(String.valueOf(worker.getTotalRewarded()));
                                }else if (id != null && id.compareTo("tableLastMakeSpan") == 0) {
                                    ((Label) label).setText("-");
                                }else if (id != null && id.compareTo("tableTimesSubmitted") == 0) {
                                    ((Label) label).setText("-");
                                }else if (id != null && id.compareTo("tableBestMakeSpan") == 0) {
                                    ((Label) label).setText("-");
                                }
                            }
                    }
            }
            table.getChildren().add(node);
        }
    }

    public void handlerExit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void handlerAttachWorkers(ActionEvent actionEvent) throws IOException {
        int num = Integer.parseInt(workersNum.getText());
        if(num>0){
            for (int i = 0; i < num; i++) {
                WorkerRI worker = new WorkerImpl(client,jobGroupRI.getWorkersSize()+1, jobGroupRI.getJobOwner(),new State(),jobGroupRI.getJobName());
                System.out.println(worker.getId());
                jobGroupRI.attachWorker(worker);
            }
        }else{
            workersNum.clear();
            infoMessage.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
            infoMessage.setText("Number of workers can't be 0!");
        }
        infoMessage.setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green
        infoMessage.setText("Workers were attached to job successfully!");
        workersNum.clear();
        updateJobWorkers();
        insertWorkersInTable();
    }

    private void updateJobWorkers() throws RemoteException {
        workersMap=client.userSessionRI.getWorkersMap(jobGroupRI.getJobName());
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

    public void showWorkerbuttons(int workerID) throws RemoteException {
        if(jobGroupRI.getJobWorkers().get(workerID).getOwner().compareTo(client.userSessionRI.getUsername())==0){
            btnsWorkers.setVisible(true);
        }
    }

    public void clearMessage(MouseEvent mouseEvent) {
        infoMessage.setText("");
    }

    public void printHashMap(Map<Integer, WorkerRI> hashMap) throws RemoteException {
        Collection<WorkerRI> workers = hashMap.values();
        for (WorkerRI worker : workers) {
            System.out.println(worker.getOwner());
        }
    }
}

