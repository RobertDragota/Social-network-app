package com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo;

import com.socialnetwork.map_toysocialnetwork.Domain.Account;
import com.socialnetwork.map_toysocialnetwork.Domain.Friendship;
import com.socialnetwork.map_toysocialnetwork.Domain.Tuple;
import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Repository.JDB_Utils;
import com.socialnetwork.map_toysocialnetwork.Repository.Repo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AccountsRepo implements Repo<Tuple<Long, String>, Account> {

    private final JDB_Utils DB_connection;
    private final Validator<Account> accountValidator;

    public AccountsRepo(String URL, String username, String password, Validator<Account> accountValidator) {
        DB_connection = new JDB_Utils(URL,username,password);
        this.accountValidator = accountValidator;
    }

    @Override
    public Optional<Account> add(Account account) {
        String Query = "INSERT INTO accounts(userid,username,password) VALUES (?,?,?)";
        try {
            accountValidator.validate(account);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            statement.setLong(1, account.getId().first());
            statement.setString(2, account.getUsername());
            statement.setString(3, account.getPassword());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(account);
    }

    @Override
    public Optional<Account> update(Account account) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> delete(Account account) {
        String query = "DELETE FROM accounts WHERE userid = ?";
        try {
            accountValidator.validate(account);

            Optional<Account> existingAccount = find(account.getId());
            if (existingAccount.isEmpty()) {
                throw new IllegalArgumentException("Error from AccountsRepo (delete): This acoount doesn't exist!");
            }

            try (PreparedStatement statement = DB_connection.getConnection().prepareStatement(query)) {
                statement.setLong(1, account.getId().first());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Deletion failed, no rows affected.");
                }
            }

        } catch (SQLException | ValidException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(account);
    }

    @Override
    public Optional<Account> find(Tuple<Long, String> longStringTuple) {
        String Query = "SELECT * from accounts WHERE username = ? ";
        Account account = null;
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);
            statement.setString(1, longStringTuple.second());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                account = getAccount(resultSet);
            }

        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return Optional.ofNullable(account);
    }

    @Override
    public Iterable<Account> getAll() {
        Set<Account> AllAccounts = new HashSet<>();
        Account account;
        String Query = "SELECT * from friendships";
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long UserID = resultSet.getLong("userid");
                String UserName = resultSet.getString("username");
                String Password = resultSet.getString("password");
                account = new Account(UserID,UserName,Password);
                account.setId(new Tuple<>(UserID, UserName));
                try {
                    accountValidator.validate(account);
                } catch (ValidException exp) {
                    System.out.println(exp.getMessage());
                }
                AllAccounts.add(account);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return AllAccounts;
    }

    @Override
    public Iterable<Account> getFromIndexToOffset(Long index, Long offset) {
        return null;
    }

    private Account getAccount(ResultSet resultSet) throws SQLException {
        Account account;
        Tuple<Long, String> UserID = new Tuple<>(resultSet.getLong("userid"), resultSet.getString("username"));

        String Username = resultSet.getString("username");
        String Password = resultSet.getString("password");
        account = new Account(UserID.first(),Username, Password);
        account.setId(UserID);
        try {
            accountValidator.validate(account);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        return account;
    }

}