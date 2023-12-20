package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.FriendshipRequest;
import com.socialnetwork.map_toysocialnetwork.Domain.Status;
import com.socialnetwork.map_toysocialnetwork.Domain.Tuple;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.FriendshipRequestRepo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.time.LocalDateTime;
import java.util.Optional;

public class ServiceFriendshipRequest implements ServiceFriendshipRequestInterface {
    private final FriendshipRequestRepo friendshipRequestRepo;
    private final Validator<FriendshipRequest> validator;

    public ServiceFriendshipRequest(FriendshipRequestRepo friendshipRequestRepo, Validator<FriendshipRequest> validator) {


        this.friendshipRequestRepo = friendshipRequestRepo;
        this.validator = validator;
    }

    @Override
    public FriendshipRequest CreateFriendshipRequest(Long from, Long to) {
        try {
            FriendshipRequest friendshipRequest = new FriendshipRequest(from, to);
            friendshipRequest.setId(new Tuple<>(from, to));
            validator.validate(friendshipRequest);
            return friendshipRequest;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public FriendshipRequest AddFriendshipRequest(Long from, Long to) {
        try {
            if (friendshipRequestRepo.find(new Tuple<>(from, to)).isPresent())
                throw new ValidException("This friendshipRequest already exist!(From ServiceUsers: AddUser)");
            FriendshipRequest friendshipRequest = CreateFriendshipRequest(from, to);
            validator.validate(friendshipRequest);
            friendshipRequestRepo.add(friendshipRequest);
            return friendshipRequest;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public FriendshipRequest UpdateFriendshipRequest(Long from, Long to, Status status, LocalDateTime date) {
        try {
            Optional<FriendshipRequest> friendshipRequest = friendshipRequestRepo.find(new Tuple<>(from, to));
            if (friendshipRequest.isEmpty())
                throw new ValidException("This friendshipRequest doesn't exist!(From ServiceUsers: AddUser)");
            friendshipRequest.get().setDate(date);
            friendshipRequest.get().setStatus(status);
            friendshipRequestRepo.update(friendshipRequest.get());
            friendshipRequest.ifPresent(validator::validate);
          //  friendshipRequest.ifPresent(friendshipRequestRepo::add);
            return friendshipRequest.get();
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public FriendshipRequest DeleteFriendshipRequest(Long from, Long to) {
        try {
            Optional<FriendshipRequest> friendshipRequest=friendshipRequestRepo.find(new Tuple<>(from, to));
            if (friendshipRequest.isEmpty())
                throw new ValidException("This friendshipRequest doesn't  exist!(From ServiceFriendshipRequest: DeleteFriendshipRequest)");
            validator.validate(friendshipRequest.get());
            friendshipRequestRepo.delete(friendshipRequest.get());
            return friendshipRequest.get();
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public FriendshipRequest FindFriendshipRequest(Long from, Long to) {
        try {
            Optional<FriendshipRequest> friendshipRequest=friendshipRequestRepo.find(new Tuple<>(from, to));
            if (friendshipRequest.isEmpty())
                throw new ValidException("This friendshipRequest doesn't exist!(From ServiceFriendshipRequest: FindFriendshipRequest)");
            friendshipRequest.ifPresent(validator::validate);
            return friendshipRequest.get();
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<FriendshipRequest> GetAllFriendshipRequest() {
        return friendshipRequestRepo.getAll();
    }
}
