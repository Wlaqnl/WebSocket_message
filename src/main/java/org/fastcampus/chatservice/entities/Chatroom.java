package org.fastcampus.chatservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chatroom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chatroom_id")
    @Id
    Long id;

    String title;

    @OneToMany(mappedBy = "chatroom")
    Set<MemberChatroomMapping> memberChatroomMappingSet;

    LocalDateTime createdAt;

    @Transient
    Boolean hasNewMessage;

    public void setHasNewMessage(Boolean hasNewMessage){
        this.hasNewMessage = hasNewMessage;
    }

    public MemberChatroomMapping addMember(Member member){
        if(this.getMemberChatroomMappingSet() == null){
            this.memberChatroomMappingSet = new HashSet<>();
        }

        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(this)
                .build();

        this.memberChatroomMappingSet.add(memberChatroomMapping);

        return memberChatroomMapping;
    }
}
