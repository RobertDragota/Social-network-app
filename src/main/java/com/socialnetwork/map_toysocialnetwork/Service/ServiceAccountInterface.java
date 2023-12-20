package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.Account;
import com.socialnetwork.map_toysocialnetwork.Domain.Account;

public interface ServiceAccountInterface {
    Account CreateAccount(Long ID,String Username, String Password);

    Account AddAccount(Long ID,String Username, String Password);
    Account UpdateAccount(Long ID,String Username,String Password);
    Account DeleteAccount(String Username);
    Account FindAccount(String Username);
    Iterable<Account> GetAllAccount();
}

