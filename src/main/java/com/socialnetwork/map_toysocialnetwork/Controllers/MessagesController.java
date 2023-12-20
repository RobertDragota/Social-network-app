package com.socialnetwork.map_toysocialnetwork.Controllers;

import com.socialnetwork.map_toysocialnetwork.Domain.Message;
import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Service.ServiceController;
import com.socialnetwork.map_toysocialnetwork.Utils.Events.ChangeEvent;
import com.socialnetwork.map_toysocialnetwork.Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessagesController implements Observer<ChangeEvent> {
    public Button SendToAll;
    public Button ReplyButton;
    @FXML
    private ListView<User> UsersListView;
    @FXML
    private ListView<Message> MessageListView;
    @FXML
    private TextField MessageTextField;
    @FXML
    private Button SendButton;
    private final ObservableList<User> observableUsersList = FXCollections.observableArrayList();
    private final ObservableList<Message> observableMessagesList = FXCollections.observableArrayList();
    private ServiceController serviceController;
    private static User activUser;
    private  User friend;
    static void setActiveUser(User activUser) {
        MessagesController.activUser = activUser;

    }

    private void initUsers() {
        Iterable<User> users = serviceController.GetFriends(activUser.getId());
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        observableUsersList.setAll(userList);
    }
    private void initMessages(User friend) {
        Iterable<Message> messages = serviceController.GetConversation(activUser.getId(),friend.getId());
        List<Message> messageList = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        observableMessagesList.setAll(messageList);
    }
    @FXML
    public void initialize() {

        UsersListView.setItems(observableUsersList);

        this.UsersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.UsersListView.setOnMouseClicked(event -> {
            User selectedFriend = UsersListView.getSelectionModel().getSelectedItem();

            if (selectedFriend != null && !selectedFriend.equals(friend)) {
                friend = selectedFriend;
                initMessages(friend);
                MessageListView.setCellFactory(listView -> new MessageListCell());

                MessageListView.setItems(observableMessagesList);
            }
        });

    }

    public void handleSendToAll(ActionEvent actionEvent) {
        List<User> all = UsersListView.getSelectionModel().getSelectedItems();

        String text = MessageTextField.getText();
        ArrayList<Long> to = new ArrayList<>();

        for(User it: all)
            to.add(it.getId());

        Message message = new Message(text,activUser.getId(), to,null);
        try
        {
            serviceController.AddMessage(message);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.MessageTextField.clear();
        friend = UsersListView.getSelectionModel().getSelectedItem();
        if(friend != null)
        {
            initMessages(friend);
            MessageListView.setItems(observableMessagesList);
        }
    }

    public void handleReply(ActionEvent actionEvent) {
        String text = MessageTextField.getText();
        ArrayList<Long> to = new ArrayList<>();
        Message MessageToReply=MessageListView.getSelectionModel().getSelectedItem();
        to.add(friend.getId());
        Message message = new Message(text,MessagesController.activUser.getId(), to, MessageToReply.getId());
        try {
            serviceController.AddMessage(message);
            update(new ChangeEvent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        MessageTextField.clear();
    }

    private static class MessageListCell extends ListCell<Message> {
        private final HBox hbox = new HBox();
        private final TextField textField = new TextField();


        public MessageListCell() {
            super();
            hbox.getChildren().add(textField);
            HBox.setMargin(textField, new Insets(5));

            setGraphic(hbox);

            // Additional styling if needed
            textField.getStyleClass().add("rounded-textfield");
        }

        @Override
        protected void updateItem(Message message, boolean empty) {
            super.updateItem(message, empty);

            if (empty || message == null) {
                setText(null);
                setGraphic(null);
            } else {
                textField.setText(message.getMessage());

                if(message.getReply()!=0)
                    textField.setStyle("-fx-background-color: #92eca1");
                else textField.setStyle(" -fx-background-color: #ecf0f1");
                // Rest of your styling and alignment logic here
                if (Objects.equals(message.getFrom(), MessagesController.activUser.getId())) {
                    // Mesajul a fost trimis de utilizatorul activ (aliniere la dreapta)
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    textField.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    // Mesajul a fost trimis de alt utilizator (aliniere la stânga)
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    textField.setAlignment(Pos.CENTER_LEFT);
                }

                setGraphic(hbox);
            }
        }
    }




    public void setService(ServiceController serviceController) {
        this.serviceController = serviceController;
        serviceController.addObserver(this);
        initUsers();
        //initMessages(UsersListView.getSelectionModel().getSelectedItem());
    }
    @Override
    public void update(ChangeEvent changeEvent) {
        initUsers();
        initMessages(friend);
    }

    public void handleSend(ActionEvent actionEvent) {
        String text = MessageTextField.getText();
        ArrayList<Long> to = new ArrayList<>();

        to.add(friend.getId());
        Message message = new Message(text,MessagesController.activUser.getId(), to, null);
        try {
            serviceController.AddMessage(message);
            update(new ChangeEvent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        MessageTextField.clear();
    }
}
