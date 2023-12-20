package com.socialnetwork.map_toysocialnetwork.Controllers;

import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Service.ServiceController;
import com.socialnetwork.map_toysocialnetwork.Utils.Events.ChangeEvent;
import com.socialnetwork.map_toysocialnetwork.Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController implements Observer<ChangeEvent> {
    @FXML
    private TextField userIdTextField;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    private ServiceController serviceController;
    private final ObservableList<User> observableList = FXCollections.observableArrayList();

    private static User activUser;
    @FXML
    private TableColumn<User,String> tableColumnFirstName;
    @FXML
    private TableColumn<User,String> tableColumnLastName;
    @FXML
    private TableColumn<User,Long> tableColumnId;

     static void setActiveUser(User activUser) {
        FriendsController.activUser = activUser;
    }

    public void handleFindUser(ActionEvent actionEvent) {
    }

    public void setService(ServiceController serviceController) {
        this.serviceController = serviceController;
        serviceController.addObserver(this);
        initModel();
    }
    private void initModel() {
        Iterable<User> users = serviceController.GetFriends(activUser.getId());
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        observableList.setAll(userList);
    }
    @FXML
    public void initialize() {

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userIdTextField.setText(Long.toString(newValue.getId()));
                firstNameTextField.setText(newValue.getFirstName());
                lastNameTextField.setText(newValue.getLastName());
            }
        });

        tableView.setItems(observableList);
    }
    @Override
    public void update(ChangeEvent changeEvent) {
    initModel();
    }
}
