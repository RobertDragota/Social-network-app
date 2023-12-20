package com.socialnetwork.map_toysocialnetwork.Service;
import com.socialnetwork.map_toysocialnetwork.Domain.Account;
import com.socialnetwork.map_toysocialnetwork.Domain.Tuple;
import com.socialnetwork.map_toysocialnetwork.Domain.User;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.AccountsRepo;
import com.socialnetwork.map_toysocialnetwork.Repository.Entityes_Repo.UserRepo;
import com.socialnetwork.map_toysocialnetwork.Validation.ValidException;
import com.socialnetwork.map_toysocialnetwork.Validation.Validator;
import java.util.Optional;

public class ServiceAccount implements ServiceAccountInterface {

    private final AccountsRepo accountsRepo;
    private final UserRepo userRepo;
    private final Validator<Account> validator;

    public ServiceAccount(AccountsRepo accountsRepo, UserRepo userRepo, Validator<Account> validator) {
        this.accountsRepo = accountsRepo;
        this.userRepo = userRepo;
        this.validator = validator;
    }

    @Override
    public Account CreateAccount(Long ID,String Username, String Password) {
        try {
            Account account = new Account(ID, Username, Password);
            account.setId(new Tuple<>(ID, Username));
            validator.validate(account);
            return account;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Account AddAccount(Long ID,String Username, String Password) {
        try{
            Account account=CreateAccount(ID,Username,Password);
            validator.validate(account);
            accountsRepo.add(account);
            return account;
        }catch (ValidException e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    @Override
    public Account UpdateAccount(Long ID, String Username, String Password) {
        try {
            Account account = FindAccount(Username);
            validator.validate(account);
            accountsRepo.update(account);
            return account;
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Account DeleteAccount(String Username) {
        try {
            Optional<Account> account = accountsRepo.find(new Tuple<>(-1L, Username));
            account.ifPresent(validator::validate);
            account.ifPresent(accountsRepo::delete);
            return account.orElse(null);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }
    @Override
    public Account FindAccount(String Username) {
        try {
            Optional<Account> account = accountsRepo.find(new Tuple<>(-1L, Username));
            account.ifPresent(validator::validate);
            return account.orElse(null);
        } catch (ValidException exp) {
            System.out.println(exp.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Account> GetAllAccount() {
        return accountsRepo.getAll();
    }
}
