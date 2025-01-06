package org.fastcampus.chatservice.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberChatroomMapping {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_chatroom_mapping_id")
    @Id
    Long id;

    @JoinColumn(name="member_id")
    @ManyToOne
    Member member;

    @JoinColumn(name="chatroom_id")
    @ManyToOne
    Chatroom chatroom;

}
