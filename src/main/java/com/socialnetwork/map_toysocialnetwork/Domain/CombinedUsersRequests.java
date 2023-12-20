package com.socialnetwork.map_toysocialnetwork.Domain;

import java.time.LocalDateTime;

public class CombinedUsersRequests {
    private Long from;
    private Long to;
    private LocalDateTime dateTime;
    private String FirstName;
    private String LastName;
    private Status status;

    

    public CombinedUsersRequests(User user, FriendshipRequest friendshipRequest) {
        dateTime = friendshipRequest.getDate();
        FirstName = user.getFirstName();
        LastName = user.getLastName();
        status = friendshipRequest.getStatus();
        from=friendshipRequest.getFrom();
        to=friendshipRequest.getTo();
    }
    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
