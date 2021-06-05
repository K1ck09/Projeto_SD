package edu.ufp.inf.sd.rmq.client;

import edu.ufp.inf.sd.rmq.server.JobGroupRI;
import edu.ufp.inf.sd.rmq.server.State;
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
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JobController extends UnicastRemoteObject implements JobControllerRI {

    public Label jobName;
    public Label jobOwner;
    public Label jobStrat;
    public Label jobReward;
    public Label jobWorkers;
    public Label jobState;
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
    public Label menuJobLabel;
    public Label menuWorkerLabel;
    public VBox btnsJob;
    public Label infoMessage;
    public VBox btnsWorkers;
    public Label jobBestResult;
    private JobShopClient client;
    private JobGroupRI jobGroupRI;
    private Map<Integer, WorkerRI> workersMap = new HashMap<>();
    private WorkerRI selectedWorker = null;

    public JobController() throws RemoteException {
    }

    /**
     * Initialize Job variables to show in GUI
     * @param client - JobShopClient
     * @param jobGroupRI - Instance of JobGroupRI
     * @throws IOException
     */
    public void init(JobShopClient client, JobGroupRI jobGroupRI) throws IOException {
        this.client = client;
        this.jobGroupRI = jobGroupRI;
        this.client.userSessionRI.removeFromList(this.client.userSessionRI.getUsername());
        this.jobGroupRI.addToList(this, this.client.userSessionRI.getUsername());
        updateJobItem();
        if (this.client.userSessionRI.getUsername().compareTo(jobGroupRI.getJobOwner()) == 0) {
            btnsJob.setVisible(true);
        }
        updateUser();
        workersMap = jobGroupRI.getJobWorkers();
        if (!workersMap.isEmpty()) {
            insertWorkersInTable();
        }
    }

    /**
     * Updates User credits, username and display in GUI
     */
    private void updateUser() {
        Platform.runLater(
                () -> {
                    try {
                        menuCredits.setText(this.client.userSessionRI.getCredits());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        menuUsername.setText(this.client.userSessionRI.getUsername());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Updates Job variables and display in GUI
     */
    private void updateJobItem() {
        Platform.runLater(
                () -> {
                    try {
                        jobName.setText(jobGroupRI.getJobName());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        jobOwner.setText(jobGroupRI.getJobOwner());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        jobStrat.setText(jobGroupRI.getJobStrat());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(jobStrat.getText().compareTo("TabuSearch")==0){
                            jobReward.setText(jobGroupRI.getJobReward());
                        } else {
                            if(jobGroupRI.getJobReward()!=null){
                                jobReward.setText(jobGroupRI.getJobReward());
                            }else{
                                jobReward.setText("-");
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        jobWorkers.setText(String.valueOf(jobGroupRI.getWorkersSize()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (jobGroupRI.getState().compareTo("Available") == 0) {
                            jobState.setStyle("-fx-text-fill: #0dbc00");
                            jobState.setText(jobGroupRI.getState());
                        } else if (jobGroupRI.getState().compareTo("OnGoing") == 0) {
                            jobState.setStyle("-fx-text-fill: #238f65");
                            jobState.setText(jobGroupRI.getState());
                        } else if (jobGroupRI.getState().compareTo("Paused") == 0) {
                            jobState.setStyle("-fx-text-fill: #c38700");
                            jobState.setText(jobGroupRI.getState());
                        } else if (jobGroupRI.getState().compareTo("Finished") == 0) {
                            jobState.setStyle("-fx-text-fill: #ff3232");
                            jobState.setText(jobGroupRI.getState());
                        }else if (jobGroupRI.getState().compareTo("Waiting") == 0) {
                            jobState.setStyle("-fx-text-fill: #de7426");
                            jobState.setText(jobGroupRI.getState());
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(jobStrat.getText().compareTo("TabuSearch")==0){
                            jobWorkload.setText(jobGroupRI.getWorkload());
                        } else {
                            jobWorkload.setText("-");
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        jobBestResult.setText(jobGroupRI.getBestResut());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * For loop through all workers to get their information then creates a
     * ItemWorkerController and append it to the scroll panel in Job GUI
     */
    private void insertWorkersInTable() {
        Platform.runLater(() -> {
            table.getChildren().clear();
            Collection<WorkerRI> workerList = workersMap.values();
            for (WorkerRI worker : workerList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/tableWorker.fxml"));
                try {
                    Parent menuParent = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                                        try {
                                            ((Label) label).setText(String.valueOf(worker.getId()));
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (id != null && id.compareTo("tableWorkerState") == 0) {
                                        try {
                                            if (worker.getState().getCurrentState().compareTo("Ongoing") == 0) {
                                                ((Label) label).setStyle("-fx-text-fill: #0dbc00");
                                                ((Label) label).setText(worker.getState().getCurrentState());
                                            } else if (worker.getState().getCurrentState().compareTo("Paused") == 0) {
                                                ((Label) label).setStyle("-fx-text-fill: #c38700"); //238f65
                                                ((Label) label).setText(worker.getState().getCurrentState());
                                            } else if (worker.getState().getCurrentState().compareTo("Stopped") == 0) {
                                                ((Label) label).setStyle("-fx-text-fill: #ff3232");
                                                ((Label) label).setText(worker.getState().getCurrentState());
                                            }else if (worker.getState().getCurrentState().compareTo("Waiting") == 0) {
                                                ((Label) label).setStyle("-fx-text-fill: #de7426");
                                                ((Label) label).setText(worker.getState().getCurrentState());
                                            }
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (id != null && id.compareTo("tableWorkerOwner") == 0) {
                                        try {
                                            ((Label) label).setText(worker.getOwner().getUsername());
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (id != null && id.compareTo("tableRewarded") == 0) {
                                        try {
                                            if (worker.getTotalRewarded() != 0) {
                                                ((Label) label).setStyle("-fx-text-fill: #0dbc00");
                                                ((Label) label).setText(String.valueOf(worker.getTotalRewarded()));
                                            } else {
                                                ((Label) label).setText(String.valueOf(worker.getTotalRewarded()));
                                            }
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (id != null && id.compareTo("tableLastMakeSpan") == 0) {
                                        try {
                                            if(jobStrat.getText().compareTo("TabuSearch")==0){
                                                ((Label) label).setText(String.valueOf(worker.getCurrentMakespan()));
                                            }else{
                                                ((Label) label).setText("-");
                                            }
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (id != null && id.compareTo("tableTimesSubmitted") == 0) {
                                        try {
                                            if(jobStrat.getText().compareTo("TabuSearch")==0){
                                                ((Label) label).setText(worker.getTotalShares() + "/" + jobGroupRI.getWorkload());
                                            }else{
                                                if(jobGroupRI.getWorkload()!=null){
                                                    ((Label) label).setText(worker.getTotalShares() + "/" + jobGroupRI.getWorkload());
                                                }else{
                                                    ((Label) label).setText("-");
                                                }
                                            }
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (id != null && id.compareTo("tableBestMakeSpan") == 0) {
                                        try {
                                            if(worker.getBestMakespan()==Integer.MAX_VALUE){
                                                ((Label) label).setText("-");
                                            }else{
                                                ((Label) label).setText(String.valueOf(worker.getBestMakespan()));
                                            }
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        }
                }
                table.getChildren().add(node);
            }
        });
    }

    /**
     * Cleans all remote variables and finish programs
     * @param mouseEvent - An mouse event
     * @throws RemoteException
     */
    public void handlerExit(MouseEvent mouseEvent) throws RemoteException {
        this.jobGroupRI.removeFromList(this.client.userSessionRI.getUsername());
        this.client.userSessionRI.removeFromList(this.client.userSessionRI.getUsername());
        this.client.userSessionRI.logout();
        this.jobGroupRI = null;
        this.workersMap.clear();
        this.client = null;
        Platform.exit();
        System.exit(0);
    }

    /**
     * Reads input, creates the number of workers asked
     * and then attaches workers to job
     * @param actionEvent - An event action
     * @throws IOException
     */
    public void handlerAttachWorkers(ActionEvent actionEvent) throws IOException {
        Integer num = Integer.parseInt(workersNum.getText());
        if (num > 0) {
            for (Integer i = 0; i < num; i++) {
                WorkerRI worker = new WorkerImpl(client, jobGroupRI.getIdsSize(), client.userSessionRI.getUser(),
                        new State("Available", String.valueOf(jobGroupRI.getIdsSize())), jobGroupRI.getJobName());
                //System.out.println(worker);
                if (jobGroupRI.attachWorker(worker)) {
                    infoMessage.setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green
                    infoMessage.setText("Workers were attached to job successfully!");
                    workersNum.clear();
                    updateJobWorkers();
                    insertWorkersInTable();
                    updateJobItem();
                } else {
                    workersNum.clear();
                    infoMessage.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                    infoMessage.setText("Not possible to attach worker!");
                }
            }
        } else {
            workersNum.clear();
            infoMessage.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
            infoMessage.setText("Number of workers can't be 0!");
        }
    }

    /**
     * Gets workers map from database and updates the workers in the Job
     * @throws IOException
     */
    private void updateJobWorkers() throws IOException {
        workersMap = client.userSessionRI.getWorkersMap(jobGroupRI.getJobName());
    }

    /**
     * Pauses a worker
     * @param actionEvent - An event action
     * @throws IOException
     */
    public void handlerPauseWorker(ActionEvent actionEvent) throws IOException {
        if (selectedWorker != null) {
            selectedWorker.setState("Paused");
            infoMessage.setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green
            try {
                infoMessage.setText("Worker [" + selectedWorker.getId() + "] Paused");
            } catch (RemoteException e) {
                infoMessage.setText("Worker Paused");
            }
        }
    }

    /**
     * Resumes a worker
     * @param actionEvent - An event action
     * @throws IOException
     */
    public void handlerResumeWorker(ActionEvent actionEvent) throws IOException {
        if (selectedWorker != null) {
            selectedWorker.setState("Ongoing");
            infoMessage.setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green
            try {
                infoMessage.setText("Worker [" + selectedWorker.getId() + "] Resumed");
            } catch (RemoteException e) {
                infoMessage.setText("Worker Resumed");
            }
        }
    }

    /**
     * Deletes a worker
     * @param actionEvent - An event action
     * @throws IOException
     */
    public void handlerDeleteWorker(ActionEvent actionEvent) throws IOException {
        if (selectedWorker != null) {
            this.jobGroupRI.removeWorker(selectedWorker);
            infoMessage.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
            try {
                infoMessage.setText("Worker [" + selectedWorker.getId() + "] Deleted");
            } catch (RemoteException e) {
                infoMessage.setText("Worker Deleted");
            }
        }
    }

    /**
     * Pauses a job
     * @param actionEvent - An event action
     * @throws IOException
     */
    public void handlerPauseJob(ActionEvent actionEvent) throws IOException {
        jobGroupRI.setState("Paused");
    }

    /**
     * Resumes a job
     * @param actionEvent - An event action
     * @throws IOException
     */
    public void handlerResumeJob(ActionEvent actionEvent) throws IOException {
        jobGroupRI.setState("OnGoing");
    }

    /**
     * Deletes a job
     * @param actionEvent - An event action
     * @throws IOException
     */
    public void handlerDeleteJob(ActionEvent actionEvent) throws IOException {
        if (jobGroupRI.removeAllWorkers()) {
            this.client.userSessionRI.removeJob(this.jobGroupRI.getJobName());
            this.jobGroupRI = null;
            this.workersMap.clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/menu.fxml"));
            Parent menuParent = loader.load();
            Scene menuScene = new Scene(menuParent);
            Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            MenuController controller = loader.getController();
            controller.MenuControllerInit(this.client, "Job removed with Success");
            this.client = null;
            app_stage.setScene(menuScene);
            app_stage.setHeight(668.0);
            app_stage.setWidth(1049.0);
            app_stage.show();
        } else {
            infoMessage.setStyle("-fx-text-fill: #ff3232");
            infoMessage.setText("An Error occurred deleting the job");
        }
    }

    /**
     * Returns to home menu GUI
     * @param mouseEvent - An mouse event
     * @throws IOException
     */
    public void handlerMenuHome(MouseEvent mouseEvent) throws IOException {
        this.jobGroupRI.removeFromList(this.client.userSessionRI.getUsername());
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

    /**
     * Shows buttons related to a worker
     * @param workerID - ID of a worker
     * @throws RemoteException
     */
    public void showWorkerbuttons(Integer workerID) throws RemoteException {
        if (jobGroupRI.getJobWorkers().get(workerID).getOwner().getUsername().compareTo(client.userSessionRI.getUsername()) == 0) {
            btnsWorkers.setVisible(true);
            this.selectedWorker = this.jobGroupRI.getJobWorkers().get(workerID);
        }
    }

    public void clearMessage(MouseEvent mouseEvent) {
        infoMessage.setText("");
    }

    public void printHashMap(Map<Integer, WorkerRI> hashMap) throws RemoteException {
        Collection<WorkerRI> workers = hashMap.values();
        for (WorkerRI worker : workers) {
            System.out.println("[" + worker.getId() + "] -> " + worker.getState().getCurrentState());
        }
    }

    @Override
    public void updateGUI() throws IOException {
        updateJobItem();
        updateJobWorkers();
        insertWorkersInTable();
        updateUser();
    }
}
