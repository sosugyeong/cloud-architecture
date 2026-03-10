package com.example.member.controller;

import com.example.member.dto.CreateMemberRequest;
import com.example.member.dto.CreateMemberResponse;
import com.example.member.dto.GetMemberResponse;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<CreateMemberResponse> createMember(@RequestBody CreateMemberRequest request) {
        CreateMemberResponse result = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<GetMemberResponse> getMember(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findById(id));
    }
}
