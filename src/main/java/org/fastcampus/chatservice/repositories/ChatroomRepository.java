package org.fastcampus.chatservice.repositories;

import org.fastcampus.chatservice.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

}
