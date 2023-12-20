package com.socialnetwork.map_toysocialnetwork.Controllers;

import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Service.ServiceController;
import com.socialnetwork.map_toysocialnetwork.Utils.Events.ChangeEvent;
import com.socialnetwork.map_toysocialnetwork.Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<ChangeEvent> {


    @FXML
    private javafx.scene.control.Pagination Pagination;
    private ServiceController serviceController;
    private final ObservableList<User> observableList = FXCollections.observableArrayList();

    private static User activUser;
    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Long> tableColumnId;

    @FXML
    private TableColumn<User, String> tableColumnFirstName;

    @FXML
    private TableColumn<User, String> tableColumnLastName;

    @FXML
    private TextField userIdTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Button addUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button findUserButton;

    private  Long index;
    private  Long offset;

    public UserController() {
    }

    public void setService(ServiceController serviceController) {
        this.serviceController = serviceController;
        serviceController.addObserver(this);
        index=0L;
        offset=3L;
        initModelWithIndex(index,offset);
    }
    private void initModel() {
        Iterable<User> users = serviceController.GetNotFriends(activUser.getId());
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        observableList.setAll(userList);
    }
    private void initModelWithIndex(Long index ,Long offset) {
        Iterable<User> users = serviceController.GetNotFriendsWithIndex(activUser.getId(),index,offset);
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        observableList.setAll(userList);
    }

    private void initializePagination() {
        int pageCount = (int) Math.ceil((double) observableList.size() / 3);

        Pagination.setPageCount(pageCount);
        Pagination.setCurrentPageIndex(0);
        Pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex *3;
            int toIndex = Math.min(fromIndex + 3, observableList.size());
            tableView.setItems(FXCollections.observableArrayList(observableList.subList(fromIndex,toIndex)));
            return tableView;
        });
    }
    @FXML
    public void initialize() {

        tableColumnId.getStyleClass().add("row");
        tableColumnId.getStyleClass().add("row-cell");
        tableColumnFirstName.getStyleClass().add("row");
        tableColumnFirstName.getStyleClass().add("row-cell");
        tableColumnLastName.getStyleClass().add("row");
        tableColumnLastName.getStyleClass().add("row-cell");



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
        
        //tableView.setItems(observableList);
        initializePagination();
    }
    @FXML
    private void handleAddUser() {

        Long ID = Long.parseLong(userIdTextField.getText());
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        User newUser = serviceController.AddUser(ID, firstName, lastName);
        observableList.add(newUser);
        clearTextFields();
    }

    @FXML
    private void handleDeleteUser() {

        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            serviceController.DeleteUser(selectedUser.getId());
            observableList.remove(selectedUser);
            clearTextFields();
        }
    }

    @FXML
    private void handleFindUser() {
        long userId = Long.parseLong(userIdTextField.getText());
        User foundUser = serviceController.FindUser(userId);
        if (foundUser != null) {
           tableView.getSelectionModel().select(foundUser);

        } else {
            tableView.getSelectionModel().clearSelection();
        }
    }
    private void clearTextFields() {
        userIdTextField.clear();
        firstNameTextField.clear();
        lastNameTextField.clear();
    }
    static void  setActiveUser(User activUser){
        UserController.activUser =activUser;
    }
    @Override
    public void update(ChangeEvent changeEvent) {
        index++;
        initModelWithIndex(index*offset,offset);
    }
}
