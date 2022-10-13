package com.caiqiu.app.vo.lottery;

import lombok.Data;

@Data
public class LotteryQueryVO {
    private Long pageNo = 1L;//当前页码
    private Long pageSize = 30L;//每页显示数量
    private Long start_issueNumber = 0L;//开始期号
    private Long end_issueNumber = 0L;//结束期号
    private boolean isAsc = false;//是否升序 默认降序

}
