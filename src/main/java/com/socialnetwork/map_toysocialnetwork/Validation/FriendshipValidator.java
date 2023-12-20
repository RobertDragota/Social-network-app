package com.socialnetwork.map_toysocialnetwork.Validation;

import com.socialnetwork.map_toysocialnetwork.Domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidException {
        if(entity==null)
            throw new ValidException("Friendship can't be NULL!");
        if(entity.getId().first().equals(entity.getId().second()))
            throw new ValidException("Can't be friend with yourself");
    }
}
