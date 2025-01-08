package org.fastcampus.chatservice.dtos;

import org.fastcampus.chatservice.entities.Member;
import org.fastcampus.chatservice.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberDto(
        Long id,
        String email,
        String nickName,
        String name,
        String password,
        String confirmedPassword,
        Gender gender,
        String phoneNumber,
        LocalDate birthDay,
        String role){

        public static MemberDto from (Member member){
            return new MemberDto(
                    member.getId(),
                    member.getEmail(),
                    member.getNickName(),
                    member.getName(),
                    null,
                    null,
                    member.getGender(),
                    member.getPhoneNumber(),
                    member.getBirthDay(),
                    member.getRole()
            );
        }

        public static Member to(MemberDto memberDto){
            return Member.builder()
                    .id(memberDto.id())
                    .email(memberDto.email())
                    .nickName(memberDto.nickName())
                    .name(memberDto.name())
                    .gender(memberDto.gender())
                    .phoneNumber(memberDto.phoneNumber())
                    .birthDay(memberDto.birthDay())
                    .role(memberDto.role())
                    .build();
        }
}
