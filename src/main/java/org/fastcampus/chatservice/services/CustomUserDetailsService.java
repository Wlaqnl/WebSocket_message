package org.fastcampus.chatservice.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.chatservice.dtos.MemberDto;
import org.fastcampus.chatservice.entities.Member;
import org.fastcampus.chatservice.enums.Role;
import org.fastcampus.chatservice.repositories.MemberRepository;
import org.fastcampus.chatservice.vos.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username).get();
        member.getId();

        if(Role.fromCode(member.getRole()) != Role.CONSULTANT){
            throw new AccessDeniedException("상담사가 아닙니다.");
        }

        return new CustomUserDetails(member);
    }

    public MemberDto saveMember(MemberDto memberDto){
        Member member = MemberDto.to(memberDto);
        member.updatePassword(memberDto.password(), memberDto.confirmedPassword(), passwordEncoder);

        member = memberRepository.save(member);

        return MemberDto.from(member);
    }
}
