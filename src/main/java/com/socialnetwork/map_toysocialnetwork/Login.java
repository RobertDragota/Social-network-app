package com.socialnetwork.map_toysocialnetwork;

import com.socialnetwork.map_toysocialnetwork.Controllers.LoginController;
import com.socialnetwork.map_toysocialnetwork.Domain.*;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.*;
import com.socialnetwork.map_toysocialnetwork.Service.*;
import com.socialnetwork.map_toysocialnetwork.Validation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {
    private ServiceController serviceController;
    @Override
    public void start(Stage primaryStage) throws Exception {

        Validator<User> userValidator=new UserValidator();
        UserRepo userRepo=new UserRepo("jdbc:postgresql://localhost:5432/_SocialNetwork_","robert12","Asmodeus011235", userValidator);
        Validator<Friendship> friendshipValidator=new FriendshipValidator();
        FriendshipRepo friendshipRepo=new FriendshipRepo("jdbc:postgresql://localhost:5432/_SocialNetwork_","robert12","Asmodeus011235", friendshipValidator);
        Validator<Account> validator=new AccountValidator();
        AccountsRepo accountsRepo=new AccountsRepo("jdbc:postgresql://localhost:5432/_SocialNetwork_","robert12","Asmodeus011235",validator);
        ServiceAccount serviceAccount=new ServiceAccount(accountsRepo,userRepo,validator);
        ServiceUsers serviceUsers=new ServiceUsers(userRepo,userValidator);
        ServiceFriendship serviceFriendship=new ServiceFriendship(friendshipRepo,friendshipValidator);
        Validator<FriendshipRequest> friendshipRequestValidator=new FriendshipRequestValidator();
        FriendshipRequestRepo friendshipRequestReo=new FriendshipRequestRepo("jdbc:postgresql://localhost:5432/_SocialNetwork_","robert12","Asmodeus011235", friendshipRequestValidator);
        ServiceFriendshipRequest serviceFriendshipRequest=new ServiceFriendshipRequest(friendshipRequestReo,friendshipRequestValidator);
        Validator<Message> messageValidator =new MessageValidator();
        MessageRepo messageRepo=new MessageRepo("jdbc:postgresql://localhost:5432/_SocialNetwork_","robert12","Asmodeus011235", messageValidator);
        ServiceMessage serviceMessage=new ServiceMessage(messageRepo,messageValidator,userRepo,friendshipRepo);
        serviceController=new ServiceController(serviceFriendship,serviceUsers,serviceAccount,serviceFriendshipRequest,serviceMessage);
        primaryStage.setResizable(false);
        initView(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
    private void initView(Stage primaryStage)throws IOException {
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("Login.fxml"));
        Parent root=loginLoader.load();
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        LoginController loginController = loginLoader.getController();
        loginController.setService(serviceController);
        loginController.setStage(primaryStage);

    }
}
