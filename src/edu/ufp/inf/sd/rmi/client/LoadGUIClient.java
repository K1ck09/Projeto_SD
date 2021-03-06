package edu.ufp.inf.sd.rmi.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoadGUIClient extends Application {

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts GUI Client
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
        primaryStage.setTitle("Job Shop Scheduling");
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 460, 340));
        primaryStage.show();
    }

    /**
     * Changes scene
     * @param fxml - Scene to be loaded
     * @throws IOException
     */
    public void changeScene(String fxml)throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(pane);
        if(fxml.compareTo("layouts/login.fxml")==0){
            stage.setHeight(379.0);
            stage.setWidth(475.0);
        }
    }

}
