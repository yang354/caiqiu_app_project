package com.caiqiu.app.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginVO2 {
    @NotBlank(message = "用户账号不能为空")
    private String username;
    @NotBlank(message = "用户密码不能为空")
    private String password;
}
