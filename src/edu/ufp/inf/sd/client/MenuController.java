package edu.ufp.inf.sd.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class MenuController {

    public VBox table;
    public Button btnCreateTask;
    public Label menuUsername;
    public Label menuCredits;
    public TextField createJobName;
    public TextField createJobReward;
    public ChoiceBox<String> createJobStrategy;
    private final ObservableList<String> stratTypes = FXCollections.observableArrayList("TabuSearch", "Genetic Algorithm");
    public Label messageMenu;

    private HashMap<String, String> item = new HashMap<>();
    public JobShopClient client;

    public void MenuControllerInit(JobShopClient client) throws RemoteException {
        this.client = client;
        menuUsername.setText("Username: " + client.userSessionRI.getUsername());
        menuCredits.setText("Credits: " + client.userSessionRI.getCredits());
        createJobStrategy.setItems(stratTypes);
        createJobStrategy.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (!item.containsKey("strat")) {
                    item.put("strat", stratTypes.get(t1.intValue()));
                } else if (item.containsKey("strat") && item.get("strat").compareTo(stratTypes.get(t1.intValue())) != 0) {
                    item.replace("strat", stratTypes.get(t1.intValue()));
                }
            }
        });

        // client.userSessionRI.setCredtis(10);
    }

    public void handlerCreateTask(ActionEvent actionEvent) throws IOException {
        if (createJobName.getText() != null && createJobReward.getText() != null && item.containsKey("strat")) {
            int inputReward = Integer.parseInt(createJobReward.getText());
            int clientCredits = Integer.parseInt(client.userSessionRI.getCredits());
            if (inputReward <= clientCredits && inputReward > 0) {
                item.put("reward", createJobReward.getText());
                item.put("job", createJobName.getText());
                item.put("owner", client.userSessionRI.getUsername());
                item.put("workers", "0");
                item.put("state", "Ongoing");
                client.userSessionRI.createJob(client.userSessionRI.getUsername(), createJobName.getText());
                insertItemInTable();
                messageMenu.setStyle("-fx-text-fill: #0dbc00"); //#0dbc00 green
                messageMenu.setText("Job Created Sucessfully!");
                createJobReward.clear();
                createJobName.clear();
                createJobStrategy.getSelectionModel().selectFirst();
            } else {
                messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
                messageMenu.setText("Please verify is you have enough credits. Reward can't be '0Cr'");
            }
        } else {
            messageMenu.setStyle("-fx-text-fill: #ff3232"); //#0dbc00 green
            messageMenu.setText("Please make sure you fill all the fields.");
        }
    }

    private void insertItemInTable() throws IOException {
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
                                ((Label) label).setText(item.get("job"));
                            } else if (id != null && id.compareTo("tableOwner") == 0) {
                                ((Label) label).setText(item.get("owner"));
                            } else if (id != null && id.compareTo("tableStrat") == 0) {
                                ((Label) label).setText(item.get("strat"));
                            } else if (id != null && id.compareTo("tableReward") == 0) {
                                ((Label) label).setText(item.get("reward"));
                            } else if (id != null && id.compareTo("tableWorkers") == 0) {
                                ((Label) label).setText(item.get("workers"));
                            } else if (id != null && id.compareTo("tableState") == 0) {
                                ((Label) label).setText(item.get("state"));
                            }
                        }
                }


        }
        node.setUserData(client);
        table.getChildren().add(node);
        item.clear();
    }

    public void handlerMenuHome(MouseEvent mouseEvent) {
    }

    public void handlerLogout(MouseEvent mouseEvent) throws IOException {
        this.client.userSessionRI.logout();
        LoadGUIClient m = new LoadGUIClient();
        m.changeScene("layouts/login.fxml");
    }

    public void handlerChoiceBox(MouseEvent mouseEvent) {
        //reateJobStrategy.getItems().addAll("item1", "item2", "item3");
    }

    public void handlerExit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void printHashMap(HashMap<String, String> hashMap) {
        for (String value : hashMap.values()) {
            System.out.println("value: " + value);
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
