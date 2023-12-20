package com.socialnetwork.map_toysocialnetwork.Domain;

import java.util.Objects;

public class User extends Entity<Long> {

    private String FirstName;
    private String LastName;

    public User(String firstName, String lastName) {
        FirstName = firstName;
        LastName = lastName;

    }
    /*public Long getId() {
        return super.getId();
    }*/
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User U)) return false;
        return getId().equals(U.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    @Override
    public String toString() {
        return FirstName +" "+ LastName;
    }
}
