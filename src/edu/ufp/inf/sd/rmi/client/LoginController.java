package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.server.UserSessionRI;
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

    /**
     * Tries to create a RMI Session
     * @param url - an URL
     * @param resourceBundle - an ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clientConnection();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks login
     * @param actionEvent - An action event
     * @throws IOException
     */
    public void userLogin(ActionEvent actionEvent) throws IOException {
        checkLogin(actionEvent);
    }

    /**
     * Creates an RMI Session
     * @throws RemoteException
     */
    private void clientConnection() throws RemoteException {
        this.client = new JobShopClient();
        this.client.lookupService();
    }

    /**
     * Checks login values
     * @param actionEvent - An action event
     * @throws IOException
     */
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

    /**
     * Changes to the Job Menu Scene
     * @param actionEvent - An action event
     * @throws IOException
     */
    private void changeToMenuScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layouts/menu.fxml"));
        Parent menuParent = loader.load();
        Scene menuScene = new Scene(menuParent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MenuController controller = loader.getController();
        controller.MenuControllerInit(this.client);
        app_stage.setScene(menuScene);
        app_stage.setHeight(668.0);
        app_stage.setWidth(1049.0);
        app_stage.show();
    }

    /**
     * Creates an account
     * @param mouseEvent - A mouse event
     * @throws IOException
     */
    public void createAccount(MouseEvent mouseEvent) throws IOException {
        LoadGUIClient m = new LoadGUIClient();
        m.changeScene("layouts/register.fxml");
    }

    /**
     * Handles the exit
     * @param mouseEvent - A mouse event
     */
    public void handlerExit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }
}
