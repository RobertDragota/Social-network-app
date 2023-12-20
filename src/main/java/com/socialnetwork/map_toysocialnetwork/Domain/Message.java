package com.socialnetwork.map_toysocialnetwork.Domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long>{
    private String message;
    private Long from;
    private List<Long> to;
    private LocalDateTime dateTime;
    private Long mesageID;
    private Long reply;

    public Message(String message, Long from, List<Long> to, LocalDateTime dateTime,Long reply) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.dateTime = dateTime;
        this.reply=reply;
    }
    public Message(String message, Long from, List<Long> to,Long reply) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.dateTime = LocalDateTime.now();
        this.reply=reply;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public List<Long> getTo() {
        return to;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public Long getMesageID() {
        return mesageID;
    }

    public void setMesageID(Long mesageID) {
        this.mesageID = mesageID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return   message ;
    }
}
