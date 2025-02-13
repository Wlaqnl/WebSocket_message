package org.fastcampus.chatservice.services;

import lombok.RequiredArgsConstructor;
import org.fastcampus.chatservice.entities.Member;
import org.fastcampus.chatservice.enums.Gender;
import org.fastcampus.chatservice.repositories.MemberRepository;
import org.fastcampus.chatservice.vos.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributeMap = oAuth2User.getAttribute("kakao_account");
        String email = (String) attributeMap.get("email");
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() ->registerMember(attributeMap));

        System.out.println("gggggg : " + oAuth2User.getAttributes());
        System.out.println("kkkkkk : " + oAuth2User);

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }

    private Member registerMember(Map<String, Object> attributeMap){
        Member member = Member.builder()
                .email((String)attributeMap.get("email"))
                .nickName((String)((Map)attributeMap.get("profile")).get("nickname"))
                .name((String)attributeMap.get("name"))
                .phoneNumber((String)attributeMap.get("phoneNumber"))
                .gender(Gender.valueOf(((String)attributeMap.get("gender")).toUpperCase()))
                .birthDay(getBirthDay(attributeMap))
                .role("USER_ROLE")
                .build();

        return memberRepository.save(member);
    }

    private LocalDate getBirthDay(Map<String, Object> attributeMap){
        String birthYear = (String) attributeMap.get("birthyear");
        System.out.println("birthYear : " + birthYear);
        //String birthDay = (String) attributeMap.get("birthDay");
        String birthDay = "0903";
        System.out.println("birthDay : " + birthDay);

        return LocalDate.parse(birthYear + birthDay, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
