package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.UserRepo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.util.Optional;

public class ServiceUsers implements ServiceUsersInterface {
    private final UserRepo userRepo;
    private final Validator<User> validator;

    public ServiceUsers(UserRepo userRepo, Validator<User> validator) {
        this.userRepo = userRepo;
        this.validator = validator;
    }

    @Override
    public User CreateUser(Long ID, String FirstName, String Lastname) {
        try {
            User user = new User(FirstName, Lastname);
            user.setId(ID);
            validator.validate(user);
            return user;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public User AddUser(Long ID, String FirstName, String Lastname) {
        try {
            User user = CreateUser(ID, FirstName, Lastname);
            if (userRepo.find(ID).isPresent())
                throw new ValidException("This user already exist!(From ServiceUsers: AddUser)");
            validator.validate(user);
            userRepo.add(user);
            return user;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public User UpdateUser(Long ID, String FirstName, String Lastname) {
        try {
            User user = CreateUser(ID, FirstName, Lastname);
            validator.validate(user);
            userRepo.update(user);
            return user;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public User DeleteUser(Long ID) {
        try {
            User user = FindUser(ID);
            validator.validate(user);
            userRepo.delete(user);
            return user;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public User FindUser(Long ID) {
        try {
            Optional<User> user = userRepo.find(ID);
            user.ifPresent(validator::validate);
            return user.orElse(null);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<User> GetAllUsers() {
        return userRepo.getAll();
    }

    @Override
    public Iterable<User> GetAllUsersWithIndex(Long index, Long offset) {
        return userRepo.getFromIndexToOffset(index,offset);
    }
}
