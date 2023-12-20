package com.socialnetwork.map_toysocialnetwork.Controllers;

import com.socialnetwork.map_toysocialnetwork.Domain.Account;
import com.socialnetwork.map_toysocialnetwork.Login;
import com.socialnetwork.map_toysocialnetwork.Service.ServiceController;
import com.socialnetwork.map_toysocialnetwork.Utils.Events.ChangeEvent;
import com.socialnetwork.map_toysocialnetwork.Utils.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController implements Observer<ChangeEvent> {

    private Stage login;
    private ServiceController serviceController;
    @FXML
    private TextField UserNameTextFiled;
    @FXML
    private Label UsernameLable;
    @FXML
    private  Label PasswordLable;
    @FXML
    private PasswordField PasswordTextField;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button DeleteAccountButton;
    @FXML
    private Button ForgetButton;
    @FXML
    private Button LoginButton;
    public void handleLogin(ActionEvent Event) {
        try{
            Account account = serviceController.FindAccount(UserNameTextFiled.getText());
            if(account==null){
                throw new Exception("Invalid username!");
            }
            if(!account.getPassword().equals(PasswordTextField.getText()))
            {
                throw new Exception("Invalid password!");
            }

            FXMLLoader loader = new FXMLLoader(Login.class.getResource("app-view.fxml"));
            Parent root = loader.load();
            AppController appController = loader.getController();
            appController.setService(serviceController);
            AppController.setActiveUser(serviceController.FindUser(account.getUserID()));
            Scene scene = new Scene(root, 600,400);
            Stage stage = new Stage();
            stage.setTitle("App");
            stage.setResizable(false);
            stage.setScene(scene);
            UserNameTextFiled.clear();
            PasswordTextField.clear();
            stage.show();
            //login.close();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public LoginController() {
    }
    public void setService(ServiceController serviceController) {
        this.serviceController = serviceController;
        serviceController.addObserver(this);
    }
    public void setStage(Stage login){
        this.login=login;
    }
    public void handleRegister(ActionEvent Event) {
        try{
            FXMLLoader loader = new FXMLLoader(Login.class.getResource("Register.fxml"));
            Parent root = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setService(serviceController);
            Scene scene = new Scene(root, 600,400);
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }catch (IOException ignored){

        }

    }

    @Override
    public void update(ChangeEvent changeEvent) {

    }
}
