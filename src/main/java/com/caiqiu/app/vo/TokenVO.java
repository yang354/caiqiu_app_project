package com.caiqiu.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {
    //过期时间
    private Long expireTime; //token
    private String token;
}
