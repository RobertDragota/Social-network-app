package com.socialnetwork.map_toysocialnetwork.Domain;

import java.time.LocalDateTime;
import java.util.Objects;


public class Friendship extends Entity<Tuple<Long,Long>> {
    private LocalDateTime date;

    public Friendship(LocalDateTime date) {
        this.date = date;
    }

    public Friendship() {
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }

    @Override
    public String toString() {
        return "Friendship{" +"FirstUserID=" + getId().first()
                + ", SecondUserID=" + getId().second()+
                ", date=" + date +
                '}';
    }
}
