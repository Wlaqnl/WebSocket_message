package org.fastcampus.chatservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.chatservice.entities.Chatroom;
import org.fastcampus.chatservice.entities.Member;
import org.fastcampus.chatservice.entities.MemberChatroomMapping;
import org.fastcampus.chatservice.entities.Message;
import org.fastcampus.chatservice.repositories.ChatroomRepository;
import org.fastcampus.chatservice.repositories.MemberChatroomMappingRepository;
import org.fastcampus.chatservice.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomMappingRepository memberChatroomMappingRepository;

    public Chatroom createChatroom(Member member, String title){
        Chatroom chatroom = Chatroom.builder()
                .title(title)
                .createdAt(LocalDateTime.now())
                .build();

        chatroom = chatroomRepository.save(chatroom);

        MemberChatroomMapping memberChatroomMapping = chatroom.addMember(member);

        memberChatroomMapping = memberChatroomMappingRepository.save(memberChatroomMapping);

        return chatroom;
    }

    public Boolean joinChatroom(Member member, Long newChatroomId, Long currentChatroomId){
        if(currentChatroomId != null){
            updateLastCheckedAt(member, currentChatroomId);
        }
        if(memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), newChatroomId)){
            log.info("이미 참여한 채팅방입니다.");
            return false;
        }

        Chatroom chatroom = chatroomRepository.findById(newChatroomId).get();

        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(chatroom)
                .build();

        memberChatroomMapping = memberChatroomMappingRepository.save(memberChatroomMapping);

        return true;
    }

    private void updateLastCheckedAt(Member member, Long currentChatroomId){
        MemberChatroomMapping memberChatroomMapping = memberChatroomMappingRepository.findByMemberIdAndChatroomId(member.getId(), currentChatroomId)
                .get();
        memberChatroomMapping.updateLastCheckedAt();

        memberChatroomMappingRepository.save(memberChatroomMapping);
    }

    @Transactional
    public Boolean leaveChatroom(Member member, Long chatroomId){
        if(!memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), chatroomId)){
            log.info("참여하지 않은 방입니다.");
            return false;
        }

        memberChatroomMappingRepository.deleteByMemberIdAndChatroomId(member.getId(), chatroomId);

        return true;
    }

    public List<Chatroom> getChatroomList(Member member){
        List<MemberChatroomMapping> memberChatroomMappingList = memberChatroomMappingRepository.findAllByMemberId(member.getId());

//        List<Chatroom> chatroomList = new ArrayList<>();
//        for (MemberChatroomMapping memberChatroomMapping : memberChatroomMappingList) {
//            chatroomList.add(memberChatroomMapping.getChatroom());
//        }
//        return chatroomList;

//        System.out.println("memberChatroomMappingList > " + memberChatroomMappingList);
//        Stream<MemberChatroomMapping> memberChatroomMappingStream = memberChatroomMappingList.stream();
//        System.out.println("memberChatroomMappingStream > " + memberChatroomMappingStream);
//        memberChatroomMappingStream.forEach(m -> System.out.println(m.toString()));
//        Stream<Chatroom> chatroomStream = memberChatroomMappingStream.map(MemberChatroomMapping::getChatroom);
//        List<Chatroom> chatroomList = chatroomStream.toList();
//        return chatroomList;

        return memberChatroomMappingList.stream()
                .map(memberChatroomMapping -> {
                    Chatroom chatroom = memberChatroomMapping.getChatroom();
                    chatroom.setHasNewMessage(
                            messageRepository.existsByChatroomIdAndCreatedAtAfter(chatroom.getId(), memberChatroomMapping.getLastCheckedAt()));
                    return chatroom;
                })
                .toList();
    }

    public Message saveMessage(Member member, Long chatroomId, String text){
        Chatroom chatroom = chatroomRepository.findById(chatroomId).get();

        Message message = Message.builder()
                .text(text)
                .member(member)
                .chatroom(chatroom)
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getMessageList(Long chatroomId){
        return messageRepository.findAllByChatroomId(chatroomId);
    }
}
