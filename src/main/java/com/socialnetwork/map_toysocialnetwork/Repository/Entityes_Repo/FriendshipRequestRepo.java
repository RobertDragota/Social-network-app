package com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo;

import com.socialnetwork.map_toysocialnetwork.Domain.*;
import com.socialnetwork.map_toysocialnetwork.Repository.JDB_Utils;
import com.socialnetwork.map_toysocialnetwork.Repository.Repo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipRequestRepo implements Repo<Tuple<Long, Long>, FriendshipRequest> {
    private final JDB_Utils DB_connection;
    private final Validator<FriendshipRequest> friendshipRequestValidator;

    public FriendshipRequestRepo(String URL, String username, String password, Validator<FriendshipRequest> friendshipValidator) {
        DB_connection = new JDB_Utils(URL, username, password);
        this.friendshipRequestValidator = friendshipValidator;
    }


    @Override
    public Optional<FriendshipRequest> add(FriendshipRequest friendshipRequest) {
        String Query = "INSERT INTO friendshiprequest(\"from\",\"to\",\"status\",\"date\") VALUES (?,?,?,?)";
        try {
            friendshipRequestValidator.validate(friendshipRequest);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            statement.setLong(1, friendshipRequest.getId().first());
            statement.setLong(2, friendshipRequest.getId().second());
            statement.setString(3, friendshipRequest.getStatus().toString());
            statement.setTimestamp(4, Timestamp.valueOf(friendshipRequest.getDate()));
            statement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(friendshipRequest);
    }

    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest friendshipRequest) {

        String query = "UPDATE friendshiprequest SET status = ?, date = ? WHERE \"from\" = ? AND \"to\"=?";
        try {
            friendshipRequestValidator.validate(friendshipRequest);

            Optional<FriendshipRequest> existFriendshipRequest = find(friendshipRequest.getId());
            if (existFriendshipRequest.isEmpty()) {
                throw new IllegalArgumentException("Error from UserRepo (delete): This user doesn't exist!");
            }

            try (PreparedStatement statement = DB_connection.getConnection().prepareStatement(query)) {
                statement.setString(1,friendshipRequest.getStatus().toString());
                Timestamp date = Timestamp.valueOf(friendshipRequest.getDate());
                statement.setTimestamp(2,date);
                statement.setLong(3, friendshipRequest.getFrom());
                statement.setLong(4, friendshipRequest.getTo());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Deletion failed, no rows affected.");
                }
            }

        } catch (SQLException | ValidException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(friendshipRequest);
    }

    @Override
    public Optional<FriendshipRequest> delete(FriendshipRequest friendshipRequest) {
        String query = "DELETE FROM friendshiprequest WHERE \"from\" = ?  AND \"to\"=? ";
        try {
            friendshipRequestValidator.validate(friendshipRequest);

            Optional<FriendshipRequest> existFriendshipRequest = find(friendshipRequest.getId());
            if (existFriendshipRequest.isEmpty()) {
                throw new IllegalArgumentException("Error from UserRepo (delete): This user doesn't exist!");
            }

            try (PreparedStatement statement = DB_connection.getConnection().prepareStatement(query)) {
                statement.setLong(1, friendshipRequest.getFrom());
                statement.setLong(2, friendshipRequest.getTo());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Deletion failed, no rows affected.");
                }
            }

        } catch (SQLException | ValidException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(friendshipRequest);
    }

    @Override
    public Optional<FriendshipRequest> find(Tuple<Long, Long> ID) {
        String Query = "SELECT * from friendshiprequest WHERE \"from\"= ? AND \"to\"= ? OR \"from\" = ? AND \"to\"= ?";
        Optional<FriendshipRequest> friendshipRequest = Optional.empty();
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            statement.setLong(1, ID.first());
            statement.setLong(2, ID.second());
            statement.setLong(3, ID.second());
            statement.setLong(4, ID.first());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long UserID1 = resultSet.getLong("from");
                Long UserID2 = resultSet.getLong("to");
                Status status = Status.valueOf(resultSet.getString("status"));
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                friendshipRequest = Optional.of(new FriendshipRequest(UserID1, UserID2, status, date));
                friendshipRequest.get().setId(new Tuple<>(UserID1, UserID2));
                try {
                    friendshipRequest.ifPresent(friendshipRequestValidator::validate);
                } catch (ValidException exp) {
                    System.out.println(exp.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendshipRequest;
    }

    @Override
    public Iterable<FriendshipRequest> getAll() {
        String Query = "SELECT * FROM friendshiprequest";
        Optional<FriendshipRequest> friendshipRequest;
        Set<FriendshipRequest> AllRequests=new HashSet<>();
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long UserID1 = resultSet.getLong("from");
                Long UserID2 = resultSet.getLong("to");
                Status status = Status.valueOf(resultSet.getString("status"));
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                friendshipRequest = Optional.of(new FriendshipRequest(UserID1, UserID2, status, date));
                friendshipRequest.get().setId(new Tuple<>(UserID1, UserID2));
                try {
                    friendshipRequest.ifPresent(friendshipRequestValidator::validate);
                    AllRequests.add(friendshipRequest.get());
                } catch (ValidException exp) {
                    System.out.println(exp.getMessage());
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return AllRequests;
    }

    @Override
    public Iterable<FriendshipRequest> getFromIndexToOffset(Long index, Long offset) {
        return null;
    }
}
