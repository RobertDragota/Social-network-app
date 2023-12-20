package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.User;

public interface ServiceUsersInterface {
    User CreateUser(Long ID,String FirstName,String Lastname);
    User AddUser(Long ID,String FirstName,String Lastname);
    User UpdateUser(Long ID,String FirstName,String Lastname);
    User DeleteUser(Long ID);
    User FindUser(Long ID);
    Iterable<User> GetAllUsers();
    Iterable<User> GetAllUsersWithIndex(Long index,Long offset);
}
