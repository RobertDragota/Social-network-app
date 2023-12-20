package com.socialnetwork.map_toysocialnetwork.Validation;


import com.socialnetwork.map_toysocialnetwork.Domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidException {
        ValidUserName(entity);
    }
    
    private void ValidUserName(User entity) {
        if(entity==null)
            throw new ValidException("User can't be NULL!");
        String regex = "^[A-Z][a-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(entity.getFirstName());
        if (!matcher.find())
            throw new ValidException("First Name Invalid");
        matcher = pattern.matcher(entity.getLastName());
        if (!matcher.find())
            throw new ValidException("Last Name Invalid");
    }
}
