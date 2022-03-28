package com.forum.Vo;

import lombok.Data;

@Data
public class RegisterVo {
    private String username;

    private String password;

    private String role;

    private String avatar;

    private String nickname;

    private String email;
}
