package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.*;
import com.socialnetwork.map_toysocialnetwork.Utils.Events.ChangeEvent;
import com.socialnetwork.map_toysocialnetwork.Utils.Observable;
import com.socialnetwork.map_toysocialnetwork.Utils.Observer;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServiceController implements ServiceControllerInterface, Observable<ChangeEvent> {
    private final ServiceFriendship serviceFriendship;
    private final ServiceUsers serviceUsers;
    private final ServiceAccount serviceAccount;
    private final ServiceFriendshipRequest serviceFriendshipRequest;
    private final ServiceMessage serviceMessage;
    private final List<Observer<ChangeEvent>> observers = new ArrayList<>();

    public ServiceController(ServiceFriendship serviceFriendship, ServiceUsers serviceUsers, ServiceAccount serviceAccount, ServiceFriendshipRequest serviceFriendshipRequest, ServiceMessage serviceMessage) {
        this.serviceFriendship = serviceFriendship;
        this.serviceUsers = serviceUsers;
        this.serviceAccount = serviceAccount;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
        this.serviceMessage = serviceMessage;
    }

    @Override
    public Set<User> GetFriends(Long ID) {
        Set<User> users;
        users = ((Set<User>) serviceUsers.GetAllUsers()).stream().filter(user -> (serviceFriendship.FindFriendship(user.getId(), ID) != null || serviceFriendship.FindFriendship(ID, user.getId()) != null) &&
                !user.getId().equals(ID)).collect(Collectors.toSet());
        return users;
    }

    @Override
    public Set<User> GetNotFriends(Long ID) {
        Set<User> users;
        users = ((Set<User>) serviceUsers.GetAllUsers()).stream().filter(user -> serviceFriendship.FindFriendship(user.getId(), ID) == null && serviceFriendship.FindFriendship(ID, user.getId()) == null &&
                !user.getId().equals(ID)).collect(Collectors.toSet());
        return users;
    }

    @Override
    public Set<User> GetNotFriendsWithIndex(Long ID, Long index, Long offset) {
        Set<User> users;
        users = new HashSet<>(((Set<User>) serviceUsers.GetAllUsersWithIndex(index, offset)));
        return users;
    }

    @Override
    public List<Tuple<User, LocalDateTime>> GetFriendsFromMonth(Long ID, Integer month) {

        List<Tuple<User, LocalDateTime>> friendships = new ArrayList<>();
        Predicate<Friendship> equal = friendship -> Objects.equals(friendship.getId().first(), ID) || Objects.equals(friendship.getId().second(), ID);
        ((Set<Friendship>) serviceFriendship.GetAllFriendships())
                .stream()
                .filter(equal)
                .forEach(friend -> {
                    User user = (friend.getId().first().equals(ID)) ? serviceUsers.FindUser(friend.getId().second()) : serviceUsers.FindUser(friend.getId().first());
                    if (user != null && friend.getDate().getMonth().equals(Month.of(month)))
                        friendships.add(new Tuple<>(user, friend.getDate()));
                });
        return friendships;
    }


    @Override
    public void Showfriends(List<Tuple<User, LocalDateTime>> list) {
        System.out.println("Nume      | Prenume   | Data");
        list.forEach(x -> {
            String nume = x.first().getFirstName();
            String prenume = x.first().getLastName();
            String data = x.second().toString();
            String format = String.format("%-10s| %-10s| %s", nume, prenume, data);
            System.out.println(format);
        });
    }

    @Override
    public List<User> DFS(User u, Map<User, Boolean> visited) {
        List<User> Community = new ArrayList<>();
        visited.put(u, Boolean.TRUE);
        Community.add(u);
        GetFriends(u.getId())
                .stream()
                .filter(it -> !visited.get(it))
                .map(it -> DFS(it, visited))
                .forEach(Community::addAll);
        return Community;
    }

    @Override
    public List<User> BFS(User u) {
        Queue<User> queue = new LinkedList<>();
        Map<User, User> parent = new HashMap<>();
        Map<User, Integer> level = new HashMap<>();
        queue.add(u);
        parent.put(u, null);
        level.put(u, 0);
        User end = u;
        int maxLevel = 0;
        while (!queue.isEmpty()) {
            User current = queue.poll();
            int currentLevel = level.get(current);

            for (User friend : GetFriends(current.getId())) {
                if (!parent.containsKey(friend)) {
                    parent.put(friend, current);
                    level.put(friend, currentLevel + 1);
                    queue.add(friend);

                    if (currentLevel + 1 > maxLevel) {
                        maxLevel = currentLevel + 1;
                        end = friend;
                    }
                } else {
                    if (queue.contains(friend))
                        if (level.get(current) >= level.get(friend))
                            parent.put(friend, current);
                }
            }
        }
        List<User> longestPath = new ArrayList<>();
        while (end != null) {
            longestPath.add(end);
            end = parent.get(end);
        }
        Collections.reverse(longestPath);
        return longestPath;
    }

    @Override
    public Integer NumberOfCommunities() {
        Map<User, Boolean> visited = new HashMap<>();
        serviceUsers.GetAllUsers().forEach(x -> visited.put(x, Boolean.FALSE));
        AtomicInteger CommNumb = new AtomicInteger();
        ((Set<User>) serviceUsers.GetAllUsers())
                .stream()
                .filter(it -> !visited.get(it))
                .peek(it -> CommNumb.incrementAndGet())
                .forEach(it -> DFS(it, visited));
        return CommNumb.get();
    }

    @Override
    public List<List<User>> getAllCommunities() {
        Map<User, Boolean> visited = new HashMap<>();
        serviceUsers.GetAllUsers().forEach(x -> visited.put(x, Boolean.FALSE));
        List<List<User>> Community = new ArrayList<>();
        ((Set<User>) serviceUsers.GetAllUsers())
                .stream()
                .filter(it -> !visited.get(it))
                .map(it -> DFS(it, visited))
                .forEach(Community::add);
        return Community;
    }

    @Override
    public List<User> getMostSociableCommunity() {
        List<List<User>> aux = new ArrayList<>();
        serviceUsers.GetAllUsers().forEach(x -> aux.add(BFS(x)));
        return aux.stream()
                .max(Comparator.comparingInt(List::size))
                .orElse(Collections.emptyList());
    }

    @Override
    public Friendship CreateFriendship(Long ID1, Long ID2) {
        return serviceFriendship.CreateFriendship(ID1, ID2);
    }

    @Override
    public Friendship AddFriendship(Long ID1, Long ID2) {
        var entity = serviceFriendship.AddFriendship(ID1, ID2);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From AddFriendship in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Friendship FindFriendship(Long ID1, Long ID2) {
        return serviceFriendship.FindFriendship(ID1, ID2);
    }

    @Override
    public Iterable<Friendship> GetAllFriendships() {
        return serviceFriendship.GetAllFriendships();
    }

    @Override
    public User CreateUser(Long ID, String FirstName, String Lastname) {
        return serviceUsers.CreateUser(ID, FirstName, Lastname);
    }

    @Override
    public User AddUser(Long ID, String FirstName, String Lastname) {
        var entity = serviceUsers.AddUser(ID, FirstName, Lastname);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From AddUser in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User UpdateUser(Long ID, String FirstName, String Lastname) {
        var entity = serviceUsers.UpdateUser(ID, FirstName, Lastname);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From UpdateUser in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User DeleteUser(Long ID) {
        var entity = serviceUsers.DeleteUser(ID);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From DeleteUser in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User FindUser(Long ID) {
        return serviceUsers.FindUser(ID);
    }

    @Override
    public Iterable<User> GetAllUsers() {
        return serviceUsers.GetAllUsers();
    }

    @Override
    public Account CreateAccount(Long ID, String Username, String Password) {
        return serviceAccount.CreateAccount(ID, Username, Password);
    }

    @Override
    public Account AddAccount(Long ID, String Username, String Password) {
        var entity = serviceAccount.AddAccount(ID, Username, Password);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From AddAccount in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Account UpdateAccount(Long ID, String Username, String Password) {
        var entity = serviceAccount.UpdateAccount(ID, Username, Password);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From UpdateAccount in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Account DeleteAccount(String Username) {
        var entity = serviceAccount.DeleteAccount(Username);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From DeleteAccount in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Account FindAccount(String Username) {
        return serviceAccount.FindAccount(Username);
    }

    @Override
    public Iterable<Account> GetAllAccount() {
        return serviceAccount.GetAllAccount();
    }

    @Override
    public FriendshipRequest CreateFriendshipRequest(Long from, Long to) {
        return serviceFriendshipRequest.CreateFriendshipRequest(from, to);
    }

    @Override
    public FriendshipRequest AddFriendshipRequest(Long from, Long to) {
        var entity = serviceFriendshipRequest.AddFriendshipRequest(from, to);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From AddFriendshipRequest in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public FriendshipRequest UpdateFriendshipRequest(Long from, Long to, Status status, LocalDateTime date) {
        var entity = serviceFriendshipRequest.UpdateFriendshipRequest(from, to, status, date);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From UpdateFriendshipRequest in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public FriendshipRequest DeleteFriendshipRequest(Long from, Long to) {
        var entity = serviceFriendshipRequest.DeleteFriendshipRequest(from, to);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From DeleteFriendshipRequest in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public FriendshipRequest FindFriendshipRequest(Long from, Long to) {
        return serviceFriendshipRequest.FindFriendshipRequest(from, to);
    }

    @Override
    public Iterable<FriendshipRequest> GetAllFriendshipRequest() {
        return serviceFriendshipRequest.GetAllFriendshipRequest();
    }

    @Override
    public Iterable<FriendshipRequest> GetAllFriendshipRequestForActiveUser(User user) {
        Set<FriendshipRequest> friendshipRequests = new HashSet<>();
        serviceFriendshipRequest.GetAllFriendshipRequest().forEach(friendshipRequest -> {

            if (friendshipRequest.getTo().equals(user.getId())) {
                friendshipRequests.add(friendshipRequest);
            }
        });
        return friendshipRequests;
    }

    @Override
    public Message CreateMessage(Long from, List<Long> to, String text, Long reply) {
        return null;
    }

    @Override
    public Message AddMessage(Message message) {
        var entity = serviceMessage.AddMessage(message);
        try {
            if (entity == null)
                throw new ValidException("Entity can't be null:(From Messages in ServiceController)");
            this.notifyObservers(new ChangeEvent());
            return entity;
        } catch (ValidException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Message UpdateMessage(Long ID, String text) {
        return null;
    }

    @Override
    public Message DeleteMessage(Long ID) {
        return null;
    }

    @Override
    public Message FindMessage(Long ID) {
        return null;
    }

    @Override
    public Iterable<Message> GetAllMessage() {
        return null;
    }

    @Override
    public void acceptFriendRequest(Long from, Long to) {
        FriendshipRequest FriendRequest = serviceFriendshipRequest.FindFriendshipRequest(from, to);

        try {
            if (FriendRequest == null)
                throw new Exception("There is no friend request from user with ID " + from + "!");

            if (FriendRequest.getStatus() != Status.PENDING)
                throw new Exception("Can't accept this friend request!");
            LocalDateTime dateTime = LocalDateTime.now();
            serviceFriendshipRequest.UpdateFriendshipRequest(from, to, Status.ACCEPTED, dateTime);
            Friendship friendship = new Friendship(dateTime);
            friendship.setId(new Tuple<>(from, to));
            serviceFriendship.AddFriendship(friendship);
            this.notifyObservers(new ChangeEvent());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void declineFriendRequest(Long from, Long to) {
        FriendshipRequest FriendRequest = serviceFriendshipRequest.FindFriendshipRequest(from, to);

        try {
            if (FriendRequest == null)
                throw new Exception("There is no friend request from user with ID " + from + "!");

            if (FriendRequest.getStatus() != Status.PENDING)
                throw new Exception("Already accepted! Please delete your friend if you want to!");
            LocalDateTime dateTime = LocalDateTime.now();
            serviceFriendshipRequest.UpdateFriendshipRequest(from, to, Status.REJECTED, dateTime);
            this.notifyObservers(new ChangeEvent());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public List<Message> GetConversation(Long id1, Long id2) {
        Iterable<Message> all = serviceMessage.GetAllMessage();
        List<Message> messages = new ArrayList<>();
        if (all != null) {
            for (Message message : all)
                for (Long userID : message.getTo())
                    if (userID.equals(id2) && message.getFrom().equals(id1) || userID.equals(id1) && message.getFrom().equals(id2))
                        messages.add(message);

            messages.sort((o1, o2) -> {
                if (o1.getDateTime().isEqual(o2.getDateTime())) return 0;
                if (o1.getDateTime().isBefore(o2.getDateTime())) return -1;
                return 1;
            });
        }

        return messages;
    }

    @Override
    public void addObserver(Observer<ChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<ChangeEvent> e) {
        observers.remove(e);

    }

    @Override
    public void notifyObservers(ChangeEvent event) {
        observers.forEach(x -> x.update(event));
    }
}
