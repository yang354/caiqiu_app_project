package com.caiqiu.app.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_lottery")
public class Lottery implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 开奖时间
     */
    private LocalDateTime drawDate;

    /**
     * 期号
     */
    @TableField("Issue_number")
    private Long issueNumber;

    /**
     * 开奖号码
     */
    private Integer drawNumber;

    /**
     * 开奖号分布
     */
    private String numberDistribution;

    /**
     * 跨度
     */
    private Integer span;

    /**
     * 奇偶比
     */
    private String parity;

    /**
     * 大小比
     */
    private String sizeRatio;

    /**
     * 和值
     */
    private Integer andValue;

    /**
     * 和尾走势
     */
    private String movements;


}
