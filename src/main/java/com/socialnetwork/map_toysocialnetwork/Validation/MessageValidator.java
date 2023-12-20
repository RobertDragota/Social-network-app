package com.socialnetwork.map_toysocialnetwork.Validation;

import com.socialnetwork.map_toysocialnetwork.Domain.Message;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidException {
        if (entity == null)
            throw new ValidException("Message can't be NULL!");
        if (entity.getMessage().isEmpty() || entity.getFrom()==null || entity.getTo()==null) {
            throw new ValidException("Invalid message!");
        }
    }
}
