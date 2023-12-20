package com.socialnetwork.map_toysocialnetwork.Controllers;

import com.socialnetwork.map_toysocialnetwork.Service.ServiceController;
import com.socialnetwork.map_toysocialnetwork.Utils.Events.ChangeEvent;
import com.socialnetwork.map_toysocialnetwork.Utils.Observer;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController implements Observer<ChangeEvent> {
    @FXML
    private TextField UserIDtextField;
    private  ServiceController serviceController;

    @FXML
    private TextField LastNameTextField;
    @FXML
    private TextField PasswordTextField;
    @FXML
    private TextField UserNameTextField;
    @FXML
    private TextField FirstNameTextField;
    @FXML
    private Button CreateAccountBUtton;

    public RegisterController() {

    }

    public void handleCreateAccount(ActionEvent actionEvent) {
            Long ID=Long.parseLong(UserIDtextField.getText());
            String FirstName=FirstNameTextField.getText();
            String LastName=LastNameTextField.getText();
            String UserName=UserNameTextField.getText();
            String Password=PasswordTextField.getText();

        serviceController.AddUser(ID,FirstName,LastName);
        serviceController.AddAccount(ID,UserName,Password);

    }
    public void setService(ServiceController serviceController){
        this.serviceController=serviceController;
        serviceController.addObserver(this);
    }

    @Override
    public void update(ChangeEvent changeEvent) {

    }
}
