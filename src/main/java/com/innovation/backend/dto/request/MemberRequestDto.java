package com.innovation.backend.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MemberRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
