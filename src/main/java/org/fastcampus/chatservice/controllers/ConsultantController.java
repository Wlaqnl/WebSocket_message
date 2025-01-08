package org.fastcampus.chatservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.chatservice.dtos.MemberDto;
import org.fastcampus.chatservice.services.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/consultants")
@RequiredArgsConstructor
@Slf4j
public class ConsultantController {

    private final CustomUserDetailsService customUserDetailsService;

    @ResponseBody
    @PostMapping
    public MemberDto saveMember(@RequestBody MemberDto memberDto){
        return customUserDetailsService.saveMember(memberDto);
    }

    @GetMapping
    public String index(){
        return "consultants/index.html";
    }
}
