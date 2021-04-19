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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
                    printHashMap(item);
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

    public void handlerMenuHome(MouseEvent mouseEvent) {
    }

    public void handlerLogout(MouseEvent mouseEvent) throws IOException {
        this.client.userSessionRI.logout();
        LoadGUIClient m= new LoadGUIClient();
        m.changeScene("layouts/login.fxml");
    }

    private void insertItemInTable() throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("item.fxml"));

        //give the items some effect
        if(node instanceof HBox){
            HBox hbox= (HBox) node;
            ObservableList<Node> nodeIn = hbox.getChildren();
            for(Node label:nodeIn)
                if(label instanceof Label){
                    String id=label.getId();
                    if(id!= null && id.compareTo("tableJob")==0){
                        ((Label) label).setText(item.get("job"));
                    }
                    if(id!= null && id.compareTo("tableOwner")==0){
                        ((Label) label).setText(item.get("owner"));
                    }
                    if(id!= null && id.compareTo("tableStrat")==0){
                        ((Label) label).setText(item.get("strat"));
                    }
                    if(id!= null && id.compareTo("tableReward")==0){
                        ((Label) label).setText(item.get("reward"));
                    }
                    if(id!= null && id.compareTo("tableWorkers")==0){
                        ((Label) label).setText(item.get("workers"));
                    }
                    if(id!= null && id.compareTo("tableState")==0){
                        ((Label) label).setText(item.get("state"));
                    }
                }
        }

        node.setOnMouseEntered(event -> {
            node.setStyle("-fx-background-color:\n" +
                    "            rgba(0,0,0,0.08),\n" +
                    "            linear-gradient(#9a9a9a, #909090),\n" +
                    "            linear-gradient(ececec 0%, #e4e4e4 50%, #dddddd 51%, #e5e5e5 100%);");
        });
        node.setOnMouseExited(event -> {
            node.setStyle("-fx-background-color:\n" +
                    "            rgba(0,0,0,0.08),\n" +
                    "            linear-gradient(#9a9a9a, #909090),\n" +
                    "            linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);");
        });

        table.getChildren().add(node);
    }

    public void handlerChoiceBox(MouseEvent mouseEvent) {
        //reateJobStrategy.getItems().addAll("item1", "item2", "item3");
    }

    public void handlerExit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void printHashMap(HashMap<String,String> hashMap){
        for(String value: hashMap.values()){
            System.out.println("value: "+value);
        }
    }
}
