package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.Message;
import com.socialnetwork.map_toysocialnetwork.Domain.Tuple;
import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.FriendshipRepo;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.MessageRepo;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.UserRepo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ServiceMessage implements ServiceMessageInterface {

    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final FriendshipRepo friendshipRepo;
    private final Validator<Message>validator;

    public ServiceMessage(MessageRepo messageRepo, Validator<Message> validator, UserRepo userRepo, FriendshipRepo friendshipRepo) {
        this.messageRepo = messageRepo;
        this.validator = validator;
        this.userRepo=userRepo;

        this.friendshipRepo = friendshipRepo;
    }

    @Override
    public Message CreateMessage(Long from, List<Long> to, String text, Long reply) {
        try {
            Message message = new Message(text,from,to,reply);
            validator.validate(message);
            return message;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Message AddMessage(Message message) {
        try {

            List<Long> receivers = message.getTo();
            List<Long> FinalReveivers = new ArrayList<>();

            final String[] errorMessage = {""};
            receivers.forEach(
                    x -> {
                        User u1,u2;
                        Optional<User> find1=userRepo.find(x);
                        Optional<User> find2=userRepo.find(message.getFrom());
                        if(find2.isPresent() && find1.isPresent())
                        {
                            u1=find1.get();
                            u2=find2.get();
                            if (friendshipRepo.find(new Tuple<>(u1.getId(), u2.getId())).isPresent() ||friendshipRepo.find(new Tuple<>(u2.getId(), u1.getId())).isPresent()) {
                                FinalReveivers.add(x);
                            } else {
                                errorMessage[0] += "You are not friends with user " + x + "!\n";
                            }
                        }

                    });

            message.setTo(FinalReveivers);

            if (!FinalReveivers.isEmpty())
                messageRepo.add(message);


            if (errorMessage[0].compareTo("") != 0)
                throw new Exception(errorMessage[0]);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return message;
    }

    @Override
    public Message UpdateMessage(Long ID, String text) {
        return null;
    }

    @Override
    public Message DeleteMessage(Long ID) {
        try {
            Optional<Message> message = messageRepo.find(ID);
            message.ifPresent(validator::validate);
            message.ifPresent(messageRepo::delete);
            return message.orElse(null);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Message FindMessage(Long ID) {
        try {
            Optional<Message> message = messageRepo.find(ID);
            message.ifPresent(validator::validate);
            return message.orElse(null);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Message> GetAllMessage() {
        return messageRepo.getAll();
    }
}
