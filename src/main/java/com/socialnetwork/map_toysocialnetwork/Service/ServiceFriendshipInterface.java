package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.Friendship;

public interface ServiceFriendshipInterface {
    Friendship CreateFriendship(Long ID1,Long ID2);
    Friendship AddFriendship(Long ID1,Long ID2);
    Friendship AddFriendship(Friendship friendship);

    Friendship FindFriendship(Long ID1,Long ID2);

    Iterable<Friendship> GetAllFriendships();

}
