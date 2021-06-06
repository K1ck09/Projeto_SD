package edu.ufp.inf.sd.rmq.client;

import edu.ufp.inf.sd.rmq.server.JobGroupRI;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MenuController extends UnicastRemoteObject implements MenuControllerRI {

    private static final int MAX_TIMER = 30;
    public VBox table;
    public Button btnCreateTask;
    public Label menuUsername;
    public Label menuCredits;
    public TextField createJobName;
    public TextField createJobReward;
    public ChoiceBox<String> createJobStrategy;
    public Label messageMenu;
    public TextField createOptional;
    public Button btnFile;
    public Label displayTotalJobs;
    public Label displayTotalRewarded;
    public Label displayFinishJobs;
    public Label displayAvailableJobs;
    public Label displayOngoingJobs;
    public Label displayAtiveWorkers;
    public Label displayCreatedJobs;
    public Label displayParticipation;
    public Label displayCreditsClaimed;
    public Label displayTotalActiveWorkers;

    private final ObservableList<String> stratTypes = FXCollections.observableArrayList("Choose Strategy", "TabuSearch", "Genetic Algorithm");
    File file;

    private HashMap<String, String> item = new HashMap<>();
    private Map<String, JobGroupRI> jobGroups = new HashMap<>();
    public JobShopClient client;

    public MenuController() throws RemoteException {
    }

    public void MenuControllerInit(JobShopClient client, String message) throws IOException {
        this.client = client;
        this.client.userSessionRI.addList(this, this.client.userSessionRI.getUsername());
        //Set user info
        menuUsername.setText("Username: " + client.userSessionRI.getUsername());
        menuCredits.setText("Credits: " + client.userSessionRI.getCredits());
        messageMenu.setStyle("-fx-text-fill: #0dbc00");
        messageMenu.setText(message);
        // Set Strat choicebox
        createJobStrategy.setItems(stratTypes);
        createJobStrategy.getSelectionModel().clearAndSelect(0);
        createJobStrategy.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (!item.containsKey("strat") && stratTypes.get(t1.intValue()).compareTo("Choose Strategy") != 0) {
                    item.put("strat", stratTypes.get(t1.intValue()));
                } else if (item.containsKey("strat") && item.get("strat").compareTo(stratTypes.get(t1.intValue())) != 0) {
                    item.replace("strat", stratTypes.get(t1.intValue()));
                }
            }
        });
        // Update Joblist
        updateMenu();
        /*jobGroups = client.userSessionRI.getJobList();
        if (!jobGroups.isEmpty()) {
            insertItemsInTable();
        }*/
        //Update
        updateStatistics();
    }

    public void MenuControllerInit(JobShopClient client) throws IOException {
        this.client = client;
        //Set user info
        menuUsername.setText("Username: " + client.userSessionRI.getUsername());
        menuCredits.setText("Credits: " + client.userSessionRI.getCredits());
        // Set Strat choicebox
        createJobStrategy.setItems(stratTypes);
        createJobStrategy.getSelectionModel().clearAndSelect(0);
        createJobStrategy.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                //NEW VERSION
 /*               if (!item.containsKey("strat") && stratTypes.get(t1.intValue()).compareTo("Choose Strategy") != 0) {
                    item.put("strat", stratTypes.get(t1.intValue()));
                    createOptional.promptTextProperty().setValue("Number max of shares");
                } else if (item.containsKey("strat") && item.get("strat").compareTo(stratTypes.get(t1.intValue())) != 0) {
                    item.replace("strat", stratTypes.get(t1.intValue()));
                    createOptional.promptTextProperty().setValue("Waiting time");
                }*/
                if (!item.containsKey("strat") && stratTypes.get(t1.intValue()).compareTo("Choose Strategy") != 0) {
                    item.put("strat", stratTypes.get(t1.intValue()));
                    if(stratTypes.get(t1.intValue()).compareTo("TabuSearch") == 0){
                        createOptional.promptTextProperty().setValue("Number max of shares");
                    }else{
                        createOptional.promptTextProperty().setValue("Waiting time");
                    }
                }
                else if(item.containsKey("strat") && stratTypes.get(t1.intValue()).compareTo("TabuSearch") == 0){
                    item.replace("strat", stratTypes.get(t1.intValue()));
                    createOptional.promptTextProperty().setValue("Number max of shares");
                }else if (item.containsKey("strat") && stratTypes.get(t1.intValue()).compareTo("Genetic Algorithm") == 0){
                    item.replace("strat", stratTypes.get(t1.intValue()));
                    createOptional.promptTextProperty().setValue("Waiting time");
                }
            }
        });
        // Update Joblist
        updateMenu();
        updateStatistics();
    }


    private boolean containsJustNumbers(String reward) {
        boolean hasNumber = false;
        for (char c : reward.toCharArray()) {
            if (c >= '0' && c <= '9') {
                hasNumber = true;
            } else {
                return false;
            }
        }
        return hasNumber;
    }

    public void handleFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        file = fileChooser.showOpenDialog((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
        btnFile.setText(file.getName());
    }
    //NEW VERSION
    public void handlerCreateTask(ActionEvent actionEvent) throws IOException {
        boolean newJob = false;
        if (allFieldFilled()) {
            if (file != null) {
                if (!client.userSessionRI.isJobUnique(item.get("job"))) {
                    if (validateOptional()) {
                        if (item.get("strat").compareTo("TabuSearch") == 0) {
                            newJob = createTSjob();
                        } else if (item.get("strat").compareTo("Genetic Algorithm") == 0) {
                            newJob = createGAjob();
                        }
                        if (newJob) {
                            insertItemsInTable();
                            messageMenu.setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green
                            messageMenu.setText("Job Created Sucessfully!");
                            clearSelectionAndVariables();
                            this.client.userSessionRI.updateMenus();
                        } else {
                            messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                            messageMenu.setText("Job Creation Unsuccessful. An Error must have occured. Please try Again");
                        }
                    } else {
                        createOptional.clear();
                        messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                        messageMenu.setText("Optional only accepts numbers higher than 0 (if it's timer needs to be" +
                                " less than" + MAX_TIMER + ").");
                    }
                } else {
                    createJobName.clear();
                    messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                    messageMenu.setText("Job name need to be unique, Please enter another name");
                }
            } else {
                btnFile.setText("Choose file");
                messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                messageMenu.setText("Please select job file");
            }
        } else {
            messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
            messageMenu.setText("Please make sure you fill all the fields.");
        }
    }
    //NEW VERSION
    private boolean createGAjob() throws IOException {
        insertDataInItem();
        jobGroups = client.userSessionRI.createJob(this.client.userSessionRI, item);
        JobGroupRI currentJob = jobGroups.get(item.get("job"));
        if(currentJob != null) {
            uploadFileToJob(currentJob);
            return true;
        }
        return false;
    }
    //NEW VERSION
    private boolean createTSjob() throws IOException {
        int inputReward = Integer.parseInt(createOptional.getText());
        int clientCredits = Integer.parseInt(client.userSessionRI.getCredits());
        if (inputReward + 10 <= clientCredits && clientCredits > 0) {
            insertDataInItem();
            jobGroups = client.userSessionRI.createJob(this.client.userSessionRI, item);
            JobGroupRI currentJob = jobGroups.get(item.get("job"));
            if(currentJob != null) {
                uploadFileToJob(currentJob);
                Integer newBalance =- Integer.parseInt(item.get("load")) - 10;
                client.userSessionRI.setCredits(this.client.userSessionRI.getUser(), newBalance);
                menuCredits.setText("Credits: " + client.userSessionRI.getCredits());
                return true;
            }
        } else {
            messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
            messageMenu.setText("You don't have enough credits for the amount of shares inserted");
        }
        return false;
    }
    //NEW VERSION
    private boolean validateOptional() {
        if (item.get("strat").compareTo("TabuSearch") == 0) {
            return containsJustNumbers(createOptional.getText()) && Integer.parseInt(createOptional.getText()) > 0;
        }
        if (item.get("strat").compareTo("Genetic Algorithm") == 0) {
            return containsJustNumbers(createOptional.getText()) && Integer.parseInt(createOptional.getText()) > 0
                    && Integer.parseInt(createOptional.getText()) <= MAX_TIMER;
        }
        return false;
    }
    //NEW VERSION
    private boolean allFieldFilled() {
        return createJobName.getText() != null && item.containsKey("strat")
                && createOptional.getText() != null;
    }

    private void clearSelectionAndVariables() {
        createJobName.clear();
        createOptional.clear();
        createJobStrategy.getSelectionModel().clearAndSelect(0);
        btnFile.setText("Choose File");
        item.clear();
    }
    //NEW VERSION
    private void insertDataInItem() throws RemoteException {
        item.put("owner", client.userSessionRI.getUsername());
        item.put("workers", "0");
        if(item.get("strat").compareTo("TabuSearch")==0){
            item.put("state", "Ongoing");
        }else{
            item.put("state", "Waiting");
        }
        item.put("load", createOptional.getText()); // Can be number of shares or Timer
        item.put("job", createJobName.getText());
    }

    private void uploadFileToJob(JobGroupRI currentJob) {
        if (file != null) {
            byte[] dataToSend = new byte[(int) file.length()];
            FileInputStream in;
            try {
                in = new FileInputStream(file);
                try {
                    in.read(dataToSend, 0, dataToSend.length);
                    currentJob.uploadFile(dataToSend);
                } catch (IOException e) {
                    messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                    messageMenu.setText("Error Uploading file. Please try again");
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                    messageMenu.setText("Error Uploading file. Please try again");
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                messageMenu.setText("Error Uploading file. Please try again");
                e.printStackTrace();
            }
        }
    }

    private void insertItemsInTable() {
        Platform.runLater(
                () -> {
                    table.getChildren().clear();
                    Collection<JobGroupRI> jobsList = jobGroups.values();
                    for (JobGroupRI job : jobsList) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/tableJob.fxml"));
                        try {
                            Parent menuParent = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ItemController controller = loader.getController();
                        controller.setClient(this.client);
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
                                            if (id != null && id.compareTo("tableJob") == 0) {
                                                try {
                                                    ((Label) label).setText(job.getJobName());
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (id != null && id.compareTo("tableOwner") == 0) {
                                                try {
                                                    ((Label) label).setText(job.getJobOwner());
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (id != null && id.compareTo("tableStrat") == 0) {
                                                try {
                                                    ((Label) label).setText(job.getJobStrat());
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (id != null && id.compareTo("tableReward") == 0) {
                                                try {
                                                    if(job.getJobStrat().compareTo("TabuSearch")==0){
                                                        ((Label) label).setText(job.getJobReward());
                                                    } else {
                                                        ((Label) label).setText(" - ");
                                                    }
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (id != null && id.compareTo("tableWorkers") == 0) {
                                                try {
                                                    ((Label) label).setText(job.getWorkersSize().toString());
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            }  else if (id != null && id.compareTo("tableTimer") == 0) {
                                                try {
                                                    ((Label) label).setText(job.getTimer());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }else if (id != null && id.compareTo("tableState") == 0) {
                                                try {
                                                    if ((job.getState().compareTo("Available") == 0)) {
                                                        ((Label) label).setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green  #ff3232 red
                                                        ((Label) label).setText(job.getState());
                                                    } else if ((job.getState().compareTo("OnGoing") == 0)) {
                                                        ((Label) label).setStyle("-fx-text-fill: #238f65"); //#0dbc00 green  #ff3232 red
                                                        ((Label) label).setText(job.getState());
                                                    } else if ((job.getState().compareTo("Paused") == 0)) {
                                                        ((Label) label).setStyle("-fx-text-fill: #c38700"); //#0dbc00 green  #ff3232 red
                                                        ((Label) label).setText(job.getState());
                                                    } else if ((job.getState().compareTo("Finished") == 0)) {
                                                        ((Label) label).setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green  #ff3232 red
                                                        ((Label) label).setText(job.getState());
                                                    }else if ((job.getState().compareTo("Waiting") == 0)) {
                                                        ((Label) label).setStyle("-fx-text-fill: #de7426"); //#0dbc00 green  #ff3232 red
                                                        ((Label) label).setText(job.getState());
                                                    }
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (id != null && id.compareTo("tableWorkLoad") == 0) {
                                                try {
                                                    if(job.getJobStrat().compareTo("TabuSearch")==0){
                                                        ((Label) label).setText(job.getTotalShares() + "/" + job.getWorkload());
                                                    } else {
                                                        ((Label) label).setText(" - ");
                                                    }
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (id != null && id.compareTo("tableBestResult") == 0) {
                                                try {
                                                    ((Label) label).setText(job.getBestResut());
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

    public void handlerLogout(MouseEvent mouseEvent) throws IOException {
        this.client.userSessionRI.removeFromList(this.client.userSessionRI.getUsername());
        this.client.userSessionRI.logout();
        LoadGUIClient l = new LoadGUIClient();
        l.changeScene("layouts/login.fxml");

    }

    public void handlerChoiceBox(MouseEvent mouseEvent) {
    }

    public void handlerExit(MouseEvent mouseEvent) throws RemoteException {
        this.client.userSessionRI.removeFromList(this.client.userSessionRI.getUsername());
        this.client.userSessionRI.logout();
        this.client = null;
        this.jobGroups.clear();
        Platform.exit();
        System.exit(0);
    }

    public void printHashMap(Map<String, JobGroupRI> hashMap) throws RemoteException {
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            System.out.println("value: " + jobGroupRI.getJobName());
        }
    }

    public void printNode(Node node) {
        if (node instanceof HBox) {
            HBox hbox = (HBox) node;
            ObservableList<Node> nodeIn = hbox.getChildren();
            for (Node label : nodeIn)
                if (label instanceof Label) {
                    System.out.println(((Label) label).getText());
                }
        }
    }

    public void messageClear(MouseEvent mouseEvent) {
        messageMenu.setText("");
    }

    private void updateStatistics() throws RemoteException {
        Platform.runLater(
                () -> {
                    displayTotalJobs.setText(String.valueOf(jobGroups.size()));
                    try {
                        displayTotalRewarded.setText(String.valueOf(totalRewardedNumber()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayFinishJobs.setText(String.valueOf(finishedNumber()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayAvailableJobs.setText(String.valueOf(availableNumber()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayOngoingJobs.setText(String.valueOf(onGoingJobsNumber()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayAtiveWorkers.setText(String.valueOf(ativeWorkersNumber()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayCreatedJobs.setText(String.valueOf(userJobsNumber()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayParticipation.setText(String.valueOf(userParticipationNumber()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayCreditsClaimed.setText(String.valueOf(userCreditsClaimed()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        displayTotalActiveWorkers.setText(String.valueOf(userTotalAtiveWorkers()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
    }

    // Dá para Todas estas funções adicionando variaveis ao User
    private Integer userParticipationNumber() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            for (WorkerRI w : jobGroupRI.getJobWorkers().values()) {
                if (w.getOwner() != null) {
                    if (w.getOwner().getUsername().compareTo(this.client.userSessionRI.getUsername()) == 0) {
                        num++;
                        break;
                    }
                }
            }
        }
        return num;
    }

    private Integer userCreditsClaimed() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getBestResult() != null) {
                if (jobGroupRI.getBestResult().getOwner().getUsername().compareTo(this.client.userSessionRI.getUsername()) == 0
                        && jobGroupRI.getState().compareTo("Finished") == 0) {
                    num += Integer.parseInt(jobGroupRI.getJobReward());
                }
            }
        }
        return num;
    }

    private Integer userTotalAtiveWorkers() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            for (WorkerRI w : jobGroupRI.getJobWorkers().values()) {
                if (w.getOwner().getUsername().compareTo(this.client.userSessionRI.getUsername()) == 0
                        && w.getState().getCurrentState().compareTo("OnGoing") == 0) {
                    num++;
                }
            }
        }
        return num;
    }

    private Integer userJobsNumber() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobOwner().compareTo(client.userSessionRI.getUsername()) == 0) {
                num++;
            }
        }
        return num;
    }

    private Integer ativeWorkersNumber() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            for (WorkerRI w : jobGroupRI.getJobWorkers().values()) {
                if (w.getState().getCurrentState().compareTo("OnGoing") == 0) {
                    num++;
                }
            }
        }
        return num;
    }

    private Integer totalRewardedNumber() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobState().getCurrentState().compareTo("Finished") == 0) {
                num += Integer.parseInt(jobGroupRI.getJobReward());
            }
        }
        return num;
    }

    private Integer onGoingJobsNumber() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobState().getCurrentState().compareTo("OnGoing") == 0) {
                num++;
            }
        }
        return num;
    }

    private Integer availableNumber() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobState().getCurrentState().compareTo("Available") == 0) {
                num++;
            }
        }
        return num;
    }

    private Integer finishedNumber() throws RemoteException {
        Integer num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobState().getCurrentState().compareTo("Finished") == 0) {
                num++;
            }
        }
        return num;
    }

    @Override
    public void updateMenu() throws IOException {
        jobGroups = client.userSessionRI.getJobList();
        if (!jobGroups.isEmpty()) {
            insertItemsInTable();
        }
        updateStatistics();
    }

    public void handlerRefresh(ActionEvent actionEvent) throws IOException {
        updateMenu();
    }
}
