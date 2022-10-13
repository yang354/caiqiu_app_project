package com.caiqiu.app.vo.lottery;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LotteryEditVO {
    /**
     * 期号
     */
    //@NotBlank(message = "期号不能为空")
    private Long issueNumber;

    /**
     * 开奖号码
     */
    //@NotBlank(message = "开奖号码不能为空")
    private String drawNumber;
}
