package com.caiqiu.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {
    private Long id;//用户ID
    private String username;//用户名称
    private String avatar;//头像
    private String phone;//手机号
    private Integer userTypeId;//用户类型Id

}