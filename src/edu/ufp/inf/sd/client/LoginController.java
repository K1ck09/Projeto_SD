package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.UserSessionRI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
        checkLogin(actionEvent);
    }

    private void clientConnection() {
        this.client = new JobShopClient();
        this.client.lookupService();
    }

    private void checkLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            UserSessionRI sessionRI = this.client.userFactoryRI.login(username, password);
            if (sessionRI != null) {
                this.client.userSessionRI = sessionRI;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/menu.fxml"));
                Parent menuParent = loader.load();
                Scene menuScene = new Scene(menuParent);
                Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                MenuController controller = loader.getController();
                controller.MenuControllerInit(this.client);
                app_stage.setScene(menuScene);
                app_stage.show();
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
