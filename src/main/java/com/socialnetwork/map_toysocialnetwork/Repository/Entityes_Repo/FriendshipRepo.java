package com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo;

import com.socialnetwork.map_toysocialnetwork.Domain.Friendship;
import com.socialnetwork.map_toysocialnetwork.Domain.Tuple;
import com.socialnetwork.map_toysocialnetwork.Repository.JDB_Utils;
import com.socialnetwork.map_toysocialnetwork.Repository.Repo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipRepo implements Repo<Tuple<Long, Long>, Friendship> {

    private final JDB_Utils DB_connection;
    private final Validator<Friendship> friendshipValidator;

    public FriendshipRepo(String URL, String username, String password, Validator<Friendship> friendshipValidator) {

        this.friendshipValidator = friendshipValidator;
        DB_connection = new JDB_Utils(URL, username, password);
    }

    @Override
    public Optional<Friendship> add(Friendship friendship) {
        String Query = "INSERT INTO friendships(userid1,userid2,friendfrom) VALUES (?,?,?)";
        try {
            friendshipValidator.validate(friendship);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
        }
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            statement.setLong(1, friendship.getId().first());
            statement.setLong(2, friendship.getId().second());
            statement.setTimestamp(3, Timestamp.valueOf(friendship.getDate()));
            statement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(friendship);
    }

    @Override
    public Optional<Friendship> update(Friendship friendship) {
        return Optional.empty();
    }


    @Override
    public Optional<Friendship> delete(Friendship friendship) {
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> find(Tuple<Long, Long> ID) {
        String Query = "SELECT * from friendships WHERE userid1 = ? AND userid2= ? OR userid1 = ? AND userid2= ?";
        Optional<Friendship> friendship = Optional.empty();
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            statement.setLong(1, ID.first());
            statement.setLong(2, ID.second());
            statement.setLong(3, ID.second());
            statement.setLong(4, ID.first());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long UserID1 = resultSet.getLong("userid1");
                Long UserID2 = resultSet.getLong("userid2");
                LocalDateTime date = resultSet.getTimestamp("friendfrom").toLocalDateTime();
                friendship = Optional.of(new Friendship(date));
                friendship.get().setId(new Tuple<>(UserID1, UserID2));
                try {
                    friendshipValidator.validate(friendship.get());
                } catch (ValidException exp) {
                    System.out.println(exp.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendship;
    }

    @Override
    public Iterable<Friendship> getAll() {
        Set<Friendship> AllFriendships = new HashSet<>();
        Friendship friendship;
        String Query = "SELECT * from \"friendships\"";
        try {
            PreparedStatement statement = DB_connection.getConnection().prepareStatement(Query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long UserID1 = resultSet.getLong("userid1");
                Long UserID2 = resultSet.getLong("userid2");
                LocalDateTime date = resultSet.getTimestamp("friendfrom").toLocalDateTime();
                friendship = new Friendship(date);
                friendship.setId(new Tuple<>(UserID1, UserID2));
                try {
                    friendshipValidator.validate(friendship);
                } catch (ValidException exp) {
                    System.out.println(exp.getMessage());
                }
                AllFriendships.add(friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return AllFriendships;
    }

    @Override
    public Iterable<Friendship> getFromIndexToOffset(Long index, Long offset) {
        return null;
    }
}
