package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ServiceControllerInterface {

    Set<User> GetFriends(Long ID);
    Set<User> GetNotFriends(Long ID);
    Set<User> GetNotFriendsWithIndex(Long ID ,Long index,Long offset);
    List<Tuple<User, LocalDateTime>> GetFriendsFromMonth(Long ID, Integer month);

    List<User> DFS(User u, Map<User, Boolean> visited);
    List<User> BFS(User u);
    Integer NumberOfCommunities();

    List<List<User>> getAllCommunities();
    List<User> getMostSociableCommunity();

    Friendship CreateFriendship(Long ID1, Long ID2);
    Friendship AddFriendship(Long ID1,Long ID2);

    Friendship FindFriendship(Long ID1,Long ID2);

    Iterable<Friendship> GetAllFriendships();

    User CreateUser(Long ID,String FirstName,String Lastname);
    User AddUser(Long ID,String FirstName,String Lastname);
    User UpdateUser(Long ID,String FirstName,String Lastname);
    User DeleteUser(Long ID);
    User FindUser(Long ID);
    Iterable<User> GetAllUsers();

    Account CreateAccount(Long ID, String Username, String Password);
    Account AddAccount(Long ID, String Username, String Password);
    Account UpdateAccount(Long ID,String Username,String Password);
    Account DeleteAccount(String Username);
    Account FindAccount(String Username);
    Iterable<Account> GetAllAccount();

    FriendshipRequest CreateFriendshipRequest(Long from, Long to);
    FriendshipRequest AddFriendshipRequest(Long from,Long to);
    FriendshipRequest UpdateFriendshipRequest(Long from, Long to, Status status, LocalDateTime date);
    FriendshipRequest DeleteFriendshipRequest(Long from,Long to);
    FriendshipRequest FindFriendshipRequest(Long from,Long to);
    Iterable< FriendshipRequest> GetAllFriendshipRequest();
    Iterable< FriendshipRequest> GetAllFriendshipRequestForActiveUser(User user);

    Message CreateMessage(Long from, List<Long> to,String text,Long reply);
    Message AddMessage(Message message);
    Message UpdateMessage(Long ID,String text);
    Message DeleteMessage(Long ID);
    Message FindMessage(Long ID);
    Iterable<Message> GetAllMessage();
    List<Message> GetConversation(Long id1, Long id2);
    void acceptFriendRequest(Long from,Long to);
    void declineFriendRequest(Long from,Long to);
    void Showfriends(List<Tuple<User, LocalDateTime>> list);


}
