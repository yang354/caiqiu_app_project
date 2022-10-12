package com.caiqiu.app.config.captcha;

import lombok.Data;

@Data
public class SysConfig {
    private Long configId;
    private String configName;
    private String configValue;
    private String configKey;
    private String configType;
}
