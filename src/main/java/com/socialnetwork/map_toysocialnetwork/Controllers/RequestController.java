package com.socialnetwork.map_toysocialnetwork.Controllers;

import com.socialnetwork.map_toysocialnetwork.Domain.CombinedUsersRequests;
import com.socialnetwork.map_toysocialnetwork.Domain.FriendshipRequest;
import com.socialnetwork.map_toysocialnetwork.Domain.Status;
import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Service.ServiceController;
import com.socialnetwork.map_toysocialnetwork.Utils.Events.ChangeEvent;
import com.socialnetwork.map_toysocialnetwork.Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestController implements Observer<ChangeEvent> {
    private static User activUser;
    public javafx.scene.control.Pagination Pagination;
    @FXML
    private TableColumn<CombinedUsersRequests, LocalDateTime> DateColumn;
    @FXML
    private TableColumn<CombinedUsersRequests, String> FirstNameColumn;
    @FXML
    private TableColumn<CombinedUsersRequests, String> LastNameColumn;
    @FXML
    private TableColumn<CombinedUsersRequests, Status> StatusColumn;
    @FXML
    private TableView<CombinedUsersRequests> tableview;
    @FXML
    private ListView<User> listview;
    @FXML
    private TextField userIdTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    private ServiceController serviceController;
    private final ObservableList<User> observableListUsers = FXCollections.observableArrayList();
    private final ObservableList<CombinedUsersRequests> observableListRequests = FXCollections.observableArrayList();

    @FXML
    private Button FindButton;
    @FXML
    private Button RequestButton;
    @FXML
    private Button AcceptRequest;
    @FXML
    private Button DeclineRequest;


    public void setService(ServiceController serviceController) {
        this.serviceController = serviceController;
        serviceController.addObserver(this);
        initUsers();
        initRequests();
    }
    private void initializePagination() {
        int pageCount = (int) Math.ceil((double) observableListRequests.size() / 3);

        Pagination.setPageCount(pageCount);
        Pagination.setCurrentPageIndex(0);
        Pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex *3;
            int toIndex = Math.min(fromIndex + 3, observableListRequests.size());
            tableview.setItems(FXCollections.observableArrayList(observableListRequests.subList(fromIndex,toIndex)));
            return tableview;
        });
    }
    private void initUsers() {
        Iterable<User> users = serviceController.GetNotFriends(activUser.getId());
        List<User> userList = StreamSupport.stream(users.spliterator(), false).filter(user -> serviceController.FindFriendshipRequest(user.getId(), activUser.getId()) == null || serviceController.FindFriendshipRequest(user.getId(), activUser.getId()).getStatus().equals(Status.REJECTED))
                .collect(Collectors.toList());
        observableListUsers.setAll(userList);
    }

    private void initRequests() {
        Iterable<FriendshipRequest> requests = serviceController.GetAllFriendshipRequestForActiveUser(activUser);
        List<CombinedUsersRequests> requestsList = StreamSupport.stream(requests.spliterator(), false)
                .map(request -> new CombinedUsersRequests(serviceController.FindUser(request.getFrom()), request))
                .collect(Collectors.toList());
        observableListRequests.setAll(requestsList);
    }


    @FXML
    public void initialize() {

        DateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        FirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        initializePagination();
       // tableview.setItems(observableListRequests);

        listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userIdTextField.setText(Long.toString(newValue.getId()));
                firstNameTextField.setText(newValue.getFirstName());
                lastNameTextField.setText(newValue.getLastName());
            }
        });

        listview.setItems(observableListUsers);
    }

    static void setActiveUser(User activUser) {
        RequestController.activUser = activUser;
    }


    public void handleRequestUser(ActionEvent actionEvent) {
        User selected = listview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                serviceController.AddFriendshipRequest(activUser.getId(), selected.getId());
                update(new ChangeEvent());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No item selected!");
        }
    }

    public void handleDeclineRequest(ActionEvent actionEvent) {
        CombinedUsersRequests selected = this.tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                serviceController.declineFriendRequest(selected.getFrom(), RequestController.activUser.getId());
                serviceController.DeleteFriendshipRequest(selected.getFrom(), RequestController.activUser.getId());
                update(new ChangeEvent());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No request selected!");
        }
    }

    public void handleAcceptRequest(ActionEvent actionEvent) {
        CombinedUsersRequests selected = this.tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                serviceController.acceptFriendRequest(selected.getFrom(), RequestController.activUser.getId());
                update(new ChangeEvent());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No request selected!");
        }
    }


    public void handleFindUser(ActionEvent actionEvent) {
        long userId = Long.parseLong(userIdTextField.getText());
        User foundUser = serviceController.FindUser(userId);
        if (foundUser != null) {
            listview.getSelectionModel().select(foundUser);

        } else {
            listview.getSelectionModel().clearSelection();
        }
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        initUsers();
        initRequests();
    }
}
