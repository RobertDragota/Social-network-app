package com.socialnetwork.map_toysocialnetwork.Domain;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Tuple<Long, Long>>
{
    private Long from;
    private Long to;
    private Status status;
    private LocalDateTime date;

    public FriendshipRequest(Long from, Long to)
    {
        this.from = from;
        this.to = to;
        this.status = Status.PENDING;
        this.date = LocalDateTime.now();
    }
    public FriendshipRequest(Long from, Long to ,Status status,LocalDateTime date)
    {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public Long getFrom() { return this.from; }

    public void setFrom(Long from) { this.from = from; }

    public Long getTo() { return this.to; }

    public void setTo(Long to) { this.to = to; }

    public Status getStatus() { return this.status; }

    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getDate() { return this.date; }

    public void setDate(LocalDateTime date) { this.date = date; }
}
