package com.caiqiu.app.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginVO {
    @NotBlank(message = "用户账号不能为空")
    private String username;
    @NotBlank(message = "用户密码不能为空")
    private String password;
//    @NotBlank(message = "验证码不能为空")
//    private String code;
//    private String uuid;


}