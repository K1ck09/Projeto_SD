package edu.ufp.inf.sd.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public LoginController(){}
    @FXML
    private Button btnLogin;
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;
    @FXML
    private Label wrongUsername;
    @FXML
    private Label wrongPassword;
    @FXML
    private Label clickHere;

    private JobShopClient client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientConnection();
    }

    public void userLogin(ActionEvent actionEvent) throws IOException {

    }
    private void clientConnection(){
        this.client = new JobShopClient();
        this.client.lookupService();
        System.out.println("CHEGUEI AQUI");
    }

    private void checkLogin(){

    }

    public void createAccount(MouseEvent mouseEvent) {
    }

}
