package com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo;

import com.socialnetwork.map_toysocialnetwork.Domain.*;
import com.socialnetwork.map_toysocialnetwork.Repository.JDB_Utils;
import com.socialnetwork.map_toysocialnetwork.Repository.Repo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class MessageRepo implements Repo<Long, Message> {

    private final JDB_Utils DB_connection;
    private final Validator<Message> messageValidator;

    public MessageRepo(String URL, String username, String password, Validator<Message> messageValidator) {
        DB_connection = new JDB_Utils(URL, username, password);
        this.messageValidator = messageValidator;
    }

    @Override
    public Optional<Message> add(Message message) {
        String Query;
        if (message.getReply() == null)
            Query = "INSERT INTO messages(message, date, \"fromID\") VALUES (?,?,?) RETURNING \"MessageID\" ";
        else
            Query = "INSERT INTO messages(message, date, \"fromID\",reply) VALUES (?,?,?,?)  RETURNING \"MessageID\" ";

        try {
            messageValidator.validate(message);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            statement.setString(1, message.getMessage());
            statement.setTimestamp(2, Timestamp.valueOf(message.getDateTime()));
            statement.setLong(3, message.getFrom());
            if (message.getReply() != null)
                statement.setLong(4, message.getReply());
           ResultSet resultSet= statement.executeQuery();
            if(resultSet.next()) {

                Long messageID = resultSet.getLong(1);
                message.setId(messageID);

                List<Long> receivers = message.getTo();
                PreparedStatement statement2 = DB_connection.getConnection().prepareStatement("INSERT INTO receivers(\"toID\", \"MessageID\") VALUES (?, ?)");
                for (Long userID : receivers) {
                    statement2.setLong(1, userID);
                    statement2.setLong(2, message.getId());
                    statement2.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(message);
    }

    @Override
    public Optional<Message> update(Message message) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Message message) {
        String Query;
        Query = "DELETE FROM messages WHERE \"MessageID\"=?";

        try {
            messageValidator.validate(message);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            statement.setLong(1, message.getMesageID());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(message);
    }

    @Override
    public Optional<Message> find(Long aLong) {
        String Query = "SELECT * from messages WHERE \"MessageID\" = ? ";
        Message message = null;
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                message = getMessage(resultSet);
            }

        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return Optional.ofNullable(message);
    }

    private Message getMessage(ResultSet resultSet) throws SQLException {
        Message message;
        String text = resultSet.getString("message");
        Timestamp timestamp = resultSet.getTimestamp("date");
        LocalDateTime date = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.ofHours(0));
        Long fromID = resultSet.getLong("fromID");
        Long reply = resultSet.getLong("reply");
        Long id =resultSet.getLong("MessageID");

        PreparedStatement statement = DB_connection.getConnection().prepareStatement("SELECT * FROM receivers WHERE \"MessageID\"=?");
        statement.setLong(1, id);
        resultSet = statement.executeQuery();

        List<Long> receivers = new ArrayList<>();
        while (resultSet.next()) {
            receivers.add(resultSet.getLong("toID"));
        }

        message = new Message(text, fromID, receivers, date, reply);
        message.setId(id);
        try {
            messageValidator.validate(message);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        return message;
    }

    @Override
    public Iterable<Message> getAll() {
        Set<Message> AllMessages = new HashSet<>();
        String Query = "SELECT * from messages";
        Message message;
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                message = getMessage(resultSet);
                AllMessages.add(message);
            }

        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return AllMessages;
    }

    @Override
    public Iterable<Message> getFromIndexToOffset(Long index, Long offset) {
        return null;
    }
}
