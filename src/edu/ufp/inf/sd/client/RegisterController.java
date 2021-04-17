package edu.ufp.inf.sd.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;


public class RegisterController {
    @FXML
    public TextField usernameRegister;
    @FXML
    public PasswordField passwordRegister;
    @FXML
    public Button btnRegister;
    @FXML
    public Label missingData;
    @FXML
    public PasswordField confirmPasswordRegister;

    public void userRegister(ActionEvent actionEvent) {
        missingData.setText("para já tudo bem!");
    }

    public void returnLogin(ActionEvent actionEvent) throws IOException {
        LoadGUIClient m= new LoadGUIClient();
        m.changeScene("layouts/login.fxml");
    }
}
