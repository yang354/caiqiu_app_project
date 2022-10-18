package com.caiqiu.app.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
    private String drawDate;

    /**
     * 期号
     */
    private Long issueNumber;

    /**
     * 开奖号码
     */
    private String drawNumber;

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

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)  //自动填充
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)  //自动填充
    private Date updateTime;

    /**
     * 是否删除(0-未删除，1-已删除)
     */
    @TableLogic
    private Integer isDelete;


}
