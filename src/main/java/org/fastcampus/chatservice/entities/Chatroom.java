package org.fastcampus.chatservice.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Chatroom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chatroom_id")
    @Id
    Long id;

    String title;

    @OneToMany(mappedBy = "chatroom")
    Set<MemberChatroomMapping> memberChatroomMappingSet;

    LocalDateTime createdAt;
}
