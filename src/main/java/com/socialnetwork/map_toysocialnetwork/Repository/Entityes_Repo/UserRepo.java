package com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo;

import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Repository.JDB_Utils;
import com.socialnetwork.map_toysocialnetwork.Repository.Repo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepo implements Repo<Long, User> {

    private final JDB_Utils DB_connection;

    private final Validator<User> userValidator;

    public UserRepo(String URL, String username, String password, Validator<User> userValidator) {

        this.userValidator = userValidator;
        DB_connection = new JDB_Utils(URL, username, password);
    }

    @Override
    public Optional<User> add(User user) {
        String query = "INSERT INTO users (userid, firstname, lastname) VALUES (?, ?, ?)";
        try {
            userValidator.validate(user);

            try (PreparedStatement statement = DB_connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, user.getId());
                statement.setString(2, user.getFirstName());
                statement.setString(3, user.getLastName());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 1) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            user.setId(generatedKeys.getLong(1));
                        } else {
                            throw new SQLException("Failed to retrieve generated ID.");
                        }
                    }
                } else {
                    throw new SQLException("Insertion failed, no rows affected.");
                }
            }

        } catch (SQLException | ValidException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);
    }


    @Override
    public Optional<User> update(User user) {
        String query = "UPDATE users SET firstname = ?, lastname = ? WHERE userid = ?";
        try {
            userValidator.validate(user);

            try (PreparedStatement statement = DB_connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, user.getId());
                statement.setString(2, user.getFirstName());
                statement.setString(3, user.getLastName());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 1) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            user.setId(generatedKeys.getLong(1));
                        } else {
                            throw new SQLException("Failed to retrieve generated ID.");
                        }
                    }
                } else {
                    throw new SQLException("Insertion failed, no rows affected.");
                }
            }

        } catch (SQLException | ValidException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);

    }

    @Override
    public Optional<User> delete(User user) {
        String query = "DELETE FROM users WHERE userid = ?";
        try {
            userValidator.validate(user);

            Optional<User> existingUser = find(user.getId());
            if (existingUser.isEmpty()) {
                throw new IllegalArgumentException("Error from UserRepo (delete): This user doesn't exist!");
            }

            try (PreparedStatement statement = DB_connection.getConnection().prepareStatement(query)) {
                statement.setLong(1, user.getId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Deletion failed, no rows affected.");
                }
            }

        } catch (SQLException | ValidException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);
    }


    @Override
    public Optional<User> find(Long ID) {
        String Query = "SELECT * from \"users\" WHERE \"userid\" = ? ";
        User user = null;
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);
            statement.setLong(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUser(resultSet);
            }

        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Iterable<User> getAll() {
        Set<User> AllUsers = new HashSet<>();
        String Query = "SELECT * from \"users\"";
        User user;
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = getUser(resultSet);
                AllUsers.add(user);
            }

        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return AllUsers;
    }

    @Override
    public Iterable<User> getFromIndexToOffset(Long index, Long offset) {
        Set<User> AllUsers = new HashSet<>();
        String Query = "SELECT * from \"users\" LIMIT ? OFFSET ?";
        User user;
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);
            statement.setLong(1,offset);
            statement.setLong(2,index);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = getUser(resultSet);
                AllUsers.add(user);
            }

        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return AllUsers;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user;
        Long UserID = resultSet.getLong("userid");
        String FirstName = resultSet.getString("firstname");
        String LastName = resultSet.getString("lastname");
        user = new User(FirstName, LastName);
        user.setId(UserID);
        try {
            userValidator.validate(user);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        return user;
    }
}
