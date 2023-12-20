package com.socialnetwork.map_toysocialnetwork.Utils;


import com.socialnetwork.map_toysocialnetwork.Utils.Events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E event);
}
