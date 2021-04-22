package edu.ufp.inf.sd.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.rmi.RemoteException;


public class RegisterController {
    @FXML
    public TextField usernameRegister;
    @FXML
    public PasswordField passwordRegister;
    @FXML
    public PasswordField confirmPasswordRegister;
    @FXML
    public Button btnRegister;
    @FXML
    public Label missingData;
    public Button btnBackToLogin;

    public void userRegister(ActionEvent actionEvent) throws RemoteException {
        missingData.setStyle("-fx-text-fill: #fc0000; -fx-font-size: 12px;");
        String username = usernameRegister.getText();
        String password = passwordRegister.getText();
        String confirmPassword = confirmPasswordRegister.getText();
        if (!username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if(isInPassRequisites(password, confirmPassword)){
                System.out.println("im in 1");
                JobShopClient client= new JobShopClient();
                client.lookupService();
                if(client.userFactoryRI.register(username,password)){
                    System.out.println("im in 2");
                    missingData.setStyle("-fx-text-fill: #2fb200;");
                    missingData.setText("Register Succeded. Proceed to Login");
                    btnBackToLogin.setVisible(true);
                }else{
                    missingData.setText("Username already exists. Choose another Username.");
                    System.out.println("im out 3");

                }
            }else{
                System.out.println("im out 2");
                missingData.setText("Password must be at least 8 digits " +
                        "and \ncontain at least 1 letter and number.No special characters allowed");
            }
        }else{
            System.out.println("im out 1");
            missingData.setText("Please fill in all fields !");
        }
    }

    public void returnLogin(MouseEvent actionEvent) throws IOException {
        missingData.setStyle("-fx-text-fill: #fc0000; -fx-font-size: 12px;");
        LoadGUIClient m = new LoadGUIClient();
        m.changeScene("layouts/login.fxml");
    }

    public boolean isInPassRequisites(String pass, String confPass){
        return pass.equals(confPass) && pass.length() >= 8 && containsJustLettersAndNumbers(pass);
    }

    public boolean containsJustLettersAndNumbers(String pass){
        boolean hasLetter = false;
        boolean hasNumber = false;
        for(char c: pass.toCharArray()){
            if(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                hasLetter = true;
            }else if(c >= '0' && c <= '9'){
                hasNumber = true;
            }else{
                return false;
            }
        }
        System.out.println(hasLetter);
        System.out.println(hasNumber);
        return hasLetter && hasNumber;
    }

    public void handlerExit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void handlerBackToLogin(ActionEvent actionEvent) throws IOException {
        LoadGUIClient m = new LoadGUIClient();
        m.changeScene("layouts/login.fxml");

    }
}
