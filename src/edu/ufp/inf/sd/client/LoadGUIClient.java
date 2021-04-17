package edu.ufp.inf.sd.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadGUIClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
        primaryStage.setTitle("Job Shop Scheduling");
        primaryStage.setScene(new Scene(root, 460, 340));
        primaryStage.show();
    }


}
