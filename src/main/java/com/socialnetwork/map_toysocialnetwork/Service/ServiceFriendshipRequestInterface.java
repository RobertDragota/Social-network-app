package com.socialnetwork.map_toysocialnetwork.Service;

import com.socialnetwork.map_toysocialnetwork.Domain.FriendshipRequest;
import com.socialnetwork.map_toysocialnetwork.Domain.Status;

import java.time.LocalDateTime;

public interface ServiceFriendshipRequestInterface {
    FriendshipRequest CreateFriendshipRequest(Long ID1,Long ID2);
    FriendshipRequest AddFriendshipRequest(Long ID1,Long ID2);
    FriendshipRequest UpdateFriendshipRequest(Long ID1, Long ID2, Status status, LocalDateTime date);
    FriendshipRequest DeleteFriendshipRequest(Long ID1,Long ID2);
    FriendshipRequest FindFriendshipRequest(Long ID1,Long ID2);
    Iterable< FriendshipRequest> GetAllFriendshipRequest();
}
