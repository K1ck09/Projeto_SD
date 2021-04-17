package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.UserSessionRI;
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
    @FXML
    private Button btnLogin;
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;
    @FXML
    private Label clickHere;
    @FXML
    public Label missingData;

    private JobShopClient client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientConnection();
    }

    public void userLogin(ActionEvent actionEvent) throws IOException {
        checkLogin();
    }

    private void clientConnection() {
        this.client = new JobShopClient();
        this.client.lookupService();
    }

    private void checkLogin() throws IOException {
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            UserSessionRI sessionRI = this.client.userFactoryRI.login(username, password);
            if (sessionRI != null) {
                this.client.userSessionRI = sessionRI;
                LoadGUIClient m = new LoadGUIClient();
                m.changeScene("layouts/menu.fxml");
            } else {
                missingData.setText("Login didn't succeeded. " +
                        "Username doesn't exist or password doesn't match.");
            }
        } else {
            missingData.setText("Please insert you username and password");
        }
    }

    public void createAccount(MouseEvent mouseEvent) throws IOException {
        LoadGUIClient m = new LoadGUIClient();
        m.changeScene("layouts/register.fxml");
    }

}
