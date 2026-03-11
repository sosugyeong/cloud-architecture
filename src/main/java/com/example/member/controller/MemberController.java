package com.example.member.controller;

import com.example.member.dto.*;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<CreateMemberResponse> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        log.info("[API - LOG]: {} 멤버 생성", request.getName());
        CreateMemberResponse result = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<GetMemberResponse> getMember(
            @PathVariable("id") Long id
    ) {
        log.info("[API - LOG]: {} 정보 조회", id);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findById(id));
    }

    @PostMapping("/api/members/{id}/profile-image")
    public ResponseEntity<FileUploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") Long id
    ) {
        String key = memberService.upload(id, file);
        return ResponseEntity.ok(new FileUploadResponse(key));
    }

    @GetMapping("/api/members/{id}/profile-image")
    public ResponseEntity<GetFileResponse> getUrl(
            @PathVariable("id") Long id

    ){
        return ResponseEntity.ok(memberService.getProfileUrl(id));
    }
}
