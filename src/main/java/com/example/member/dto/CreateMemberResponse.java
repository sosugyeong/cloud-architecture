package com.example.member.dto;

import lombok.Getter;

@Getter
public class CreateMemberResponse {
    private final String name;
    private final int age;
    private final String mbti;

    public CreateMemberResponse(String name, int age, String mbti) {
        this.name = name;
        this.age = age;
        this.mbti = mbti;
    }
}
