package com.caiqiu.app.model.controller;

import com.caiqiu.app.config.captcha.ProjectConfig;
import com.caiqiu.app.config.redis.RedisService;
import com.caiqiu.app.utils.Base64;
import com.caiqiu.app.utils.IdUtils;
import com.caiqiu.app.utils.Result;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author ruoyi
 */
@Api(value = "验证码",tags = "验证码",description = "验证码")
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;
    //
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    //
    @Autowired
    private RedisService redisCache;

    /**
     * 生成验证码
     */
    @Operation(summary = "生成验证码")
    @GetMapping("/captchaImage")
    public Result getCode(HttpServletResponse response) throws IOException
    {
        HashMap ajax = new HashMap<String,String>();

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = "captcha_codes:" + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = ProjectConfig.getCaptchaType();
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, 2, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return null;
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return Result.ok(ajax);
    }
}
