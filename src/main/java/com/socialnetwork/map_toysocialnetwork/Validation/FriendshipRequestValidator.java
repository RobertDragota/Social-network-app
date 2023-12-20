package com.socialnetwork.map_toysocialnetwork.Validation;

import com.socialnetwork.map_toysocialnetwork.Domain.FriendshipRequest;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) throws ValidException {
        if(entity==null)
            throw new ValidException("Friendship can't be NULL!");
        if(entity.getId().first().equals(entity.getId().second()))
            throw new ValidException("Can't be friend with yourself");
    }
}
