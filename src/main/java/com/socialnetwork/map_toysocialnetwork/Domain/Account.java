package com.socialnetwork.map_toysocialnetwork.Domain;

public class Account extends Entity<Tuple<Long,String>>
{
    private String username;
    private String password;
    private Long userID;

    public Account(Long userID,String username, String password)
    {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }

    public Long getUserID() { return this.userID; }

    public void setUserID(Long userID) { this.userID = userID; }
}
