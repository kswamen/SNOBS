package com.back.snobs.domain.chatroom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Boolean existsBySenderSnob_userEmailAndReceiverSnob_userEmail(String senderEmail, String receiverEmail);
    @Query(value = "select distinct cr from ChatRoom cr join fetch cr.senderSnob join fetch cr.receiverSnob where cr.senderSnob.userEmail = :userEmail")
    List<ChatRoom> findAllChatRoomByMeWithFetchJoin(@Param("userEmail") String userEmail, Pageable pageable);
    @Query(value = "select distinct cr from ChatRoom cr join fetch cr.receiverSnob join fetch cr.senderSnob where cr.receiverSnob.userEmail = :userEmail")
    List<ChatRoom> findAllChatRoomByYouWithFetchJoin(@Param("userEmail") String userEmail, Pageable pageable);
    @Query(value = "select cr from ChatRoom cr join fetch cr.receiverSnob join fetch cr.senderSnob" +
            " where cr.receiverSnob.userEmail = :userEmail or cr.senderSnob.userEmail = :userEmail order by cr.chatRoomIdx")
    List<ChatRoom> findAllChatRoomByUserEmail(@Param("userEmail") String userEmail, Pageable pageable);
}
