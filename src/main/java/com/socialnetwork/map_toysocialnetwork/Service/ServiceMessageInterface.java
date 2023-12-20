package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.Message;

import java.util.List;

public interface ServiceMessageInterface {
    Message CreateMessage(Long from, List<Long> to,String text,Long reply);
    Message AddMessage(Message message);
    Message UpdateMessage(Long ID,String text);
    Message DeleteMessage(Long ID);
    Message FindMessage(Long ID);
    Iterable<Message> GetAllMessage();
}
