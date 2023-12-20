package com.socialnetwork.map_toysocialnetwork.Controllers;

import com.socialnetwork.map_toysocialnetwork.Domain.User;
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
import javafx.stage.Stage;

public class AppController implements Observer<ChangeEvent> {
    @FXML
    private Button UserButton;
    @FXML
    private Button MessagesButton;
    @FXML
    private Button FriendButton;
    @FXML
    private Button RequestsButton;
    private ServiceController serviceController;

    private static User activeUser;

    @Override
    public void update(ChangeEvent changeEvent) {

    }
    public void setService(ServiceController serviceController) {
        this.serviceController = serviceController;
        serviceController.addObserver(this);
    }

    static void setActiveUser(User activeUser){
        AppController.activeUser =activeUser;
    }

    public void handleUsers(ActionEvent actionEvent) {
        try{

            FXMLLoader loader = new FXMLLoader(Login.class.getResource("user-view.fxml"));
            Parent root = loader.load();
            UserController userController = loader.getController();
            UserController.setActiveUser(activeUser);
            userController.setService(serviceController);

            Scene scene = new Scene(root, 940,690);
            Stage stage = new Stage();
            stage.setTitle("Users");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleMessages(ActionEvent actionEvent) {
        try{

            FXMLLoader loader = new FXMLLoader(Login.class.getResource("messages.fxml"));
            Parent root = loader.load();
            MessagesController messagesController = loader.getController();
            MessagesController.setActiveUser(activeUser);
            messagesController.setService(serviceController);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Messages");
            stage.setWidth(780);
            stage.setHeight(590);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleFriends(ActionEvent actionEvent) {
        try{

            FXMLLoader loader = new FXMLLoader(Login.class.getResource("friends.fxml"));
            Parent root = loader.load();
            FriendsController friendsController = loader.getController();
            FriendsController.setActiveUser(activeUser);
            friendsController.setService(serviceController);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Friends");
            stage.setWidth(670);
            stage.setHeight(390);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleRequests(ActionEvent actionEvent) {
        try{

            FXMLLoader loader = new FXMLLoader(Login.class.getResource("requests.fxml"));
            Parent root = loader.load();
            RequestController requestController = loader.getController();
            RequestController.setActiveUser(activeUser);
            requestController.setService(serviceController);

            Scene scene = new Scene(root, 940,640);
            Stage stage = new Stage();
            stage.setTitle("Requests");
            stage.setWidth(870);
            stage.setHeight(650);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}






























