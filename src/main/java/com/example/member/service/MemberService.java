package com.example.member.service;

import com.example.member.dto.CreateMemberRequest;
import com.example.member.dto.CreateMemberResponse;
import com.example.member.dto.GetFileResponse;
import com.example.member.dto.GetMemberResponse;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public CreateMemberResponse create(CreateMemberRequest request) {
        Member member = new Member(
                request.getName(),
                request.getAge(),
                request.getMbti()
        );

        Member savedMember = memberRepository.save(member);
        return new CreateMemberResponse(
                savedMember.getName(),
                savedMember.getAge(),
                savedMember.getMbti()
        );
    }

    @Transactional(readOnly = true)
    public GetMemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        return new GetMemberResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
    }

    @Transactional
    public String upload(Long id, MultipartFile file) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("해당 멤버가 존재하지 않습니다.")
        );

        String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            s3Template.upload(bucket, key, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

        member.uploadProfileImage(key);
        return key;
    }

    @Transactional(readOnly = true)
    public GetFileResponse getProfileUrl(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("해당 멤버가 존재하지 않습니다.")
        );

        String key = member.getProfileImageUrl();
        if(key == null || key.isEmpty()) {
            throw new IllegalStateException("등록된 프로필 이미지가 없습니다.");
        }

        URL url = s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
        return new GetFileResponse(url.toString());
    }
}
