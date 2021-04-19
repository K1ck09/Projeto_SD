package edu.ufp.inf.sd.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadGUIClient extends Application {

    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
        primaryStage.setTitle("Job Shop Scheduling");
        primaryStage.setScene(new Scene(root, 460, 340));
        primaryStage.show();
    }

    public void changeScene(String fxml)throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(pane);
        if(fxml.compareTo("layouts/login.fxml")==0){
            pane.resize(460.0,340.0);
            stage.setHeight(340.0);
            stage.setWidth(460.0);
        }
    }

}
