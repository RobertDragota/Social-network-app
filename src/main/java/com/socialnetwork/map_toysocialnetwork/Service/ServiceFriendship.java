package com.socialnetwork.map_toysocialnetwork.Service;
import com.socialnetwork.map_toysocialnetwork.Domain.Friendship;
import com.socialnetwork.map_toysocialnetwork.Domain.Tuple;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.FriendshipRepo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.util.Optional;

public class ServiceFriendship implements ServiceFriendshipInterface{

    private final FriendshipRepo friendshipRepo;
    private final Validator<Friendship>validator;

    public ServiceFriendship(FriendshipRepo friendshipRepo, Validator<Friendship> validator) {
        this.friendshipRepo = friendshipRepo;
        this.validator = validator;
    }


    @Override
    public Friendship CreateFriendship(Long ID1, Long ID2) {
        try {
            Friendship friendship =new Friendship();
            friendship.setId(new Tuple<>(ID1,ID2));
            validator.validate(friendship);
            return friendship;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Friendship AddFriendship(Long ID1,Long ID2) {
        try {
            Friendship friendship =CreateFriendship(ID1,ID2);
            validator.validate(friendship);
            friendshipRepo.add(friendship);
            return friendship;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Friendship AddFriendship(Friendship friendship) {
        try {

            validator.validate(friendship);
            friendshipRepo.add(friendship);
            return friendship;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }


    @Override
    public Friendship FindFriendship(Long ID1, Long ID2) {
        try {
            Optional<Friendship> friendship =friendshipRepo.find(new Tuple<>(ID1,ID2));
            friendship.ifPresent(validator::validate);
            return friendship.orElse(null);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Friendship> GetAllFriendships() {
        return friendshipRepo.getAll();
    }

}
