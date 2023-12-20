package com.socialnetwork.map_toysocialnetwork.Validation;

import com.socialnetwork.map_toysocialnetwork.Domain.Account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidator implements Validator<Account> {
    @Override
    public void validate(Account entity) throws ValidException {
        if (entity == null)
            throw new ValidException("Account can't be NULL!");
        if (entity.getUsername().isEmpty() || entity.getUsername().contains("<>?:\"{}~_+\\!@#$%^&*()")) {
            throw new ValidException("Invalid username!");
        }
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(entity.getPassword());
        if (!matcher.find())
            throw new ValidException("Invalid password");

    }
}
