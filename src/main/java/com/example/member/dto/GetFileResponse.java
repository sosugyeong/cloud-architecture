package com.example.member.dto;

import lombok.Getter;

@Getter
public class GetFileResponse {

    private final String url;

    public GetFileResponse(String url) {
        this.url = url;
    }
}
