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
<<<<<<< HEAD
    public void start(Stage primaryStage) throws IOException {
        stage=primaryStage;
=======
    public void start(Stage primaryStage) throws Exception {
>>>>>>> parent of 611ff4e (Login/Register frontend working)
        Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
        primaryStage.setTitle("Job Shop Scheduling");
        primaryStage.setScene(new Scene(root, 460, 340));
        primaryStage.show();
    }

<<<<<<< HEAD
    public void changeScene(String fxml)throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(pane);
    }
=======
>>>>>>> parent of 611ff4e (Login/Register frontend working)

}
