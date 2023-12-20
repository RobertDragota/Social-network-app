package com.socialnetwork.map_toysocialnetwork.Utils;


import com.socialnetwork.map_toysocialnetwork.Utils.Events.Event;

public interface Observer <E extends Event> {
    void update(E e);
}
