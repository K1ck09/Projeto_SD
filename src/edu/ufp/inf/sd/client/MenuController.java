package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
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

import java.io.*;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MenuController extends LoadGUIClient {

    public VBox table;
    public Button btnCreateTask;
    public Label menuUsername;
    public Label menuCredits;
    public TextField createJobName;
    public TextField createJobReward;
    public ChoiceBox<String> createJobStrategy;
    private final ObservableList<String> stratTypes = FXCollections.observableArrayList("Choose Strategy", "TabuSearch", "Genetic Algorithm");
    public Label messageMenu;
    public Label displayTotalJobsUser;
    public Label displayAtiveWorkersUser;
    public Label displayAtiveWorkers;
    public Label displayTotalJobs;
    public Label displayFinishJobs;
    public Label displayPausedJobs;
    public Label displayOnGoingJobs;
    public Label displayFinishJobsUser;
    public Label displayPausedJobsUser;
    public Label displayOnGoingJobsUser;
    public TextField createSharesPerWorker;
    public Slider createTotalWorkload;
    public Button btnFile;
    File file;

    private HashMap<String, String> item = new HashMap<>();
    private Map<String, JobGroupRI> jobGroups = new HashMap<>();
    public JobShopClient client;

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
                if (!item.containsKey("strat") && stratTypes.get(t1.intValue()).compareTo("Choose Strategy") != 0) {
                    item.put("strat", stratTypes.get(t1.intValue()));
                } else if (item.containsKey("strat") && item.get("strat").compareTo(stratTypes.get(t1.intValue())) != 0) {
                    item.replace("strat", stratTypes.get(t1.intValue()));
                }
            }
        });
        // Update Joblist
        jobGroups = client.userSessionRI.getJobList();
        if (!jobGroups.isEmpty()) {
            insertItemsInTable();
        }
        //Update
        updateStatistics();
        // client.userSessionRI.setCredtis(10);
    }

    private void updateStatistics() throws RemoteException {
        displayTotalJobs.setText(String.valueOf(jobGroups.size()));
        displayTotalJobsUser.setText(String.valueOf(userJobsNumber()));
        displayOnGoingJobs.setText(String.valueOf(onGoingJobsNumber()));
        displayOnGoingJobs.setText(String.valueOf(onGoingJobsNumber()));

    }

    private int userJobsNumber() throws RemoteException {
        int num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobOwner().compareTo(client.userSessionRI.getUsername()) == 0) {
                num++;
            }
        }
        return num;
    }

    private int onGoingJobsNumber() throws RemoteException {
        int num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobState().getCurrentState().compareTo("Ongoing") == 0) {
                num++;
            }
        }
        return num;
    }

    private int onGoingJobsNumberUser() throws RemoteException {
        int num = 0;
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI jobGroupRI : jobsList) {
            if (jobGroupRI.getJobState().getCurrentState().compareTo("Ongoing") == 0 && jobGroupRI.getJobOwner().compareTo(client.userSessionRI.getUsername()) == 0) {
                num++;
            }
        }
        return num;
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
        file= fileChooser.showOpenDialog(stage);
        btnFile.setText(file.getName());
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws IOException {
        if (createJobName.getText() != null && createJobReward.getText() != null && item.containsKey("strat")) {
            if (containsJustNumbers(createJobReward.getText()) && containsJustNumbers(createSharesPerWorker.getText())) {
                int inputReward = Integer.parseInt(createJobReward.getText());
                int clientCredits = Integer.parseInt(client.userSessionRI.getCredits());
                if (inputReward <= clientCredits && inputReward > 0) {
                    item.put("job", createJobName.getText());
                    if (!client.userSessionRI.isJobUnique(item.get("job"))) {
                        insertDataInItem();
                        if(file!=null){
                            jobGroups = client.userSessionRI.createJob(item);
                            JobGroupRI currentJob= jobGroups.get(item.get("job"));
                            if(currentJob!=null) {
                                uploadFileToJob(currentJob);
                                // decidir se tira o dinheiro so no fim ou no inicio e depois devolve se não encontrar nada
                                int newBalance = Integer.parseInt(client.userSessionRI.getCredits()) - Integer.parseInt(item.get("reward"));
                                client.userSessionRI.setCredits(newBalance);
                                menuCredits.setText("Credits: " + client.userSessionRI.getCredits());
                                insertItemsInTable();
                                messageMenu.setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green
                                messageMenu.setText("Job Created Sucessfully!");
                                clearSelectionAndVariables();
                                updateStatistics();
                            }else{
                                messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                                messageMenu.setText("Job Creation Unsuccessful. An Error must have occured. Please try Again");
                            }
                        }else{
                            btnFile.setText("Choose file");
                            messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                            messageMenu.setText("Please select job file");
                        }
                    } else {
                        createJobName.clear();
                        messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                        messageMenu.setText("Job name need to be unique, Please enter another name");
                    }
                } else {
                    createJobReward.clear();
                    messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                    messageMenu.setText("Please verify is you have enough credits. Reward can't be '0Cr'");
                }
            } else {
                createJobReward.clear();
                messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                messageMenu.setText("You can only enter Numbers in Reward Field");
            }
        } else {
            messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
            messageMenu.setText("Please make sure you fill all the fields.");
        }
    }

    private void clearSelectionAndVariables() {
        createJobReward.clear();
        createJobName.clear();
        createSharesPerWorker.clear();
        createTotalWorkload.adjustValue(5);
        createJobStrategy.getSelectionModel().clearAndSelect(0);
        btnFile.setText("Choose File");
        item.clear();
    }

    private void insertDataInItem() throws RemoteException {
        item.put("reward", createJobReward.getText());
        item.put("owner", client.userSessionRI.getUsername());
        item.put("workers", "0");
        item.put("state", "Ongoing");
        item.put("load",String.valueOf((int) createTotalWorkload.getValue()*Integer.parseInt(createSharesPerWorker.getText()))); // min shares 10!
        item.put("shares",createSharesPerWorker.getText());
    }

    private void uploadFileToJob(JobGroupRI currentJob) {
        if(file!=null){
            byte [] dataToSend= new byte[(int) file.length()];
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

    private void insertItemsInTable() throws IOException {
        table.getChildren().clear();
        Collection<JobGroupRI> jobsList = jobGroups.values();
        for (JobGroupRI job : jobsList) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/tableJob.fxml"));
            Parent menuParent = loader.load();
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
                                    ((Label) label).setText(job.getJobName());
                                } else if (id != null && id.compareTo("tableOwner") == 0) {
                                    ((Label) label).setText(job.getJobOwner());
                                } else if (id != null && id.compareTo("tableStrat") == 0) {
                                    ((Label) label).setText(job.getJobStrat());
                                } else if (id != null && id.compareTo("tableReward") == 0) {
                                    ((Label) label).setText(job.getJobReward());
                                } else if (id != null && id.compareTo("tableWorkers") == 0) {
                                    ((Label) label).setText(job.getWorkersSize().toString());
                                } else if (id != null && id.compareTo("tableState") == 0) {
                                    ((Label) label).setText(job.getState());
                                } else if (id != null && id.compareTo("tableWorkLoad") == 0) {
                                    ((Label) label).setText("0/"+job.getWorkload());
                                } else if (id != null && id.compareTo("tableSharesPerWorker") == 0) {
                                    ((Label) label).setText(job.getSharesPerWorker());
                                }
                            }
                    }
            }
            table.getChildren().add(node);
        }
    }

    public void handlerLogout(MouseEvent mouseEvent) throws IOException {
        this.client.userSessionRI.logout();
        changeScene("layouts/login.fxml");

    }

    public void handlerChoiceBox(MouseEvent mouseEvent) {
    }

    public void handlerExit(MouseEvent mouseEvent) {
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


}
