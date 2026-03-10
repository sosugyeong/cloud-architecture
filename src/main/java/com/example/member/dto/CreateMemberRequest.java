package com.example.member.dto;

import lombok.Getter;

@Getter
public class CreateMemberRequest {
    private String name;
    private int age;
    private String mbti;
}
