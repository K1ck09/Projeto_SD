package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.UserSessionRI;
import javafx.application.Platform;
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
import java.rmi.RemoteException;
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
        try {
            clientConnection();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void userLogin(ActionEvent actionEvent) throws IOException {
        checkLogin(actionEvent);
    }

    private void clientConnection() throws RemoteException {
        this.client = new JobShopClient();
        this.client.lookupService();
    }

    private void checkLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            UserSessionRI sessionRI = this.client.userFactoryRI.login(username, password);
            if (sessionRI != null) {
                if(!sessionRI.isError())
                {
                    this.client.userSessionRI = sessionRI;
                    changeToMenuScene(actionEvent);
                }else{
                    missingData.setText("Session already initiated, Please Logout first!");
                }
            } else {
                missingData.setText("Login didn't succeeded. " +
                        "Username doesn't exist or password doesn't match.");
            }
        } else {
            missingData.setText("Please insert you username and password");
        }
    }

    private void changeToMenuScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/menu.fxml"));
        Parent menuParent = loader.load();
        Scene menuScene = new Scene(menuParent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MenuController controller = loader.getController();
        controller.MenuControllerInit(this.client);
        this.client.userSessionRI.addList(controller);
        app_stage.setScene(menuScene);
        app_stage.setHeight(668.0);
        app_stage.setWidth(1049.0);
        app_stage.show();
    }

    public void createAccount(MouseEvent mouseEvent) throws IOException {
        LoadGUIClient m = new LoadGUIClient();
        m.changeScene("layouts/register.fxml");
    }

    public void handlerExit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }
}
