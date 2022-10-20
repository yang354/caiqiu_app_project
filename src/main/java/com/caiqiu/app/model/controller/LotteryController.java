package com.caiqiu.app.model.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caiqiu.app.model.entity.Lottery;
import com.caiqiu.app.model.service.LotteryService;
import com.caiqiu.app.utils.Result;
import com.caiqiu.app.vo.lottery.LotteryAddVO;
import com.caiqiu.app.vo.lottery.LotteryEditVO;
import com.caiqiu.app.vo.lottery.LotteryQueryVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yyyz
 * @since 2022-10-11
 */
@Api(value = "开奖业务", tags = "开奖业务", description = "开奖业务")
@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    @Resource
    LotteryService lotteryService;

    /**
     * 删除一条开奖记录
     *
     * @return
     */
    @Operation(summary = "删除一条开奖记录")
    @PostMapping("/delLottery")
    public Result delLottery(Long issueNumber) {
        //1.判断期号是否存在，不存在抛出异常
        if (lotteryService.checkIssueNumber(issueNumber) <= 0L) {
            return Result.error().msg("删除失败，期号不存在");
        }

        //按期号条件删除
        QueryWrapper<Lottery> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Lottery::getIssueNumber, issueNumber);
        boolean flag = lotteryService.remove(queryWrapper);
        return flag == true ? Result.ok().msg("删除一条开奖记录成功") : Result.error().msg("删除一条开奖记录失败");
    }


    /**
     * 修改一条开奖记录
     *
     * @return
     */
    @Operation(summary = "修改一条开奖记录")
    @PostMapping("/editLottery")
    public Result editLottery( LotteryEditVO lotteryEditVO) {
        //1.判断期号是否存在，不存在抛出异常
        if (lotteryService.checkIssueNumber(lotteryEditVO.getIssueNumber()) <= 0L) {
            return Result.error().msg("修改失败，期号不存在");
        }
        Lottery lottery = new Lottery();

        char[] chars = lotteryEditVO.getDrawNumber().toCharArray();
        //2.算出开奖号分布
        char[] bubbSort = bubbSort(chars);
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i=0;i<10;i++){
            int count = 0;
            for (int j = 0; j <bubbSort.length ; j++) {
                if(Integer.parseInt(String.valueOf(bubbSort[j]))==i){
                    count++;
                }
            }
            hashMap.put(i,count);
        }
        String s = hashMap.toString().replaceAll(" ","");
        lottery.setNumberDistribution(s);


        //3.算出跨度
        char maxNum = findMaxNum(chars);
        char minNum = findMinNum(chars);
        lottery.setSpan(Integer.valueOf(maxNum - minNum));

        //4.算出奇偶比
        lottery.setParity(findOddNum(chars) + ":" + findEvenNum(chars));
        //5.算出大小比
        lottery.setSizeRatio(findMaxFour(chars) + ":" + findMinFour(chars));
        //6.算出和值
        Integer addNum = findAddNum(chars);
        lottery.setAndValue(addNum);

        //8.设置开奖号码
        lottery.setDrawNumber(lotteryEditVO.getDrawNumber());

        //8.1算出和尾走势
        // TODO: 2022/10/13
        lottery.setMovements(String.valueOf(addNum%10));

        //9.按期号条件更新

        UpdateWrapper<Lottery> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Lottery::getIssueNumber, lotteryEditVO.getIssueNumber());
        boolean flag = lotteryService.update(lottery,updateWrapper);

        return flag == true ? Result.ok().msg("修改一条开奖记录成功") : Result.error().msg("修改一条开奖记录失败");
    }

    /**
     * 增加一条开奖记录
     *
     * @return
     */
    @Operation(summary = "增加一条开奖记录")
    @PostMapping("/addLottery")
    public Result addLottery( LotteryAddVO lotteryAddVO) {
        //0.判断期号、日期是否存在
        if (lotteryService.checkIssueNumber(lotteryAddVO.getIssueNumber()) > 0L) {
            return Result.error().msg("添加失败，期号重复");
        }

        //1.获取当前时间
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String drawDate = formatter.format(date);

        if (lotteryService.checkDrawDate(drawDate) > 0L) {
            return Result.error().msg("添加失败，开奖日期重复");
        }

        Lottery lottery = new Lottery();
        //1.1 设置开奖日期
        lottery.setDrawDate(drawDate);

        char[] chars = lotteryAddVO.getDrawNumber().toCharArray();
        //2.算出开奖号分布
        char[] bubbSort = bubbSort(chars);
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i=0;i<10;i++){
            int count = 0;
            for (int j = 0; j <bubbSort.length ; j++) {
                if(Integer.parseInt(String.valueOf(bubbSort[j]))==i){
                    count++;
                }
            }
            hashMap.put(i,count);
        }


        String s = hashMap.toString().replaceAll(" ","");
        lottery.setNumberDistribution(s);



        //3.算出跨度
        char maxNum = findMaxNum(chars);
        char minNum = findMinNum(chars);
        lottery.setSpan(Integer.valueOf(maxNum - minNum));

        //4.算出奇偶比
        lottery.setParity(findOddNum(chars) + ":" + findEvenNum(chars));

        //5.算出大小比
        lottery.setSizeRatio(findMaxFour(chars) + ":" + findMinFour(chars));

        //6.算出和值
        Integer addNum = findAddNum(chars);
        lottery.setAndValue(addNum);

        //6.1算出和尾走势
        // TODO: 2022/10/13
        lottery.setMovements(String.valueOf(addNum%10));

        //7.设置期号
        lottery.setIssueNumber(lotteryAddVO.getIssueNumber());
        //8.设置开奖号码
        lottery.setDrawNumber(lotteryAddVO.getDrawNumber());


        //9.插入
        boolean flag = lotteryService.save((lottery));
        return flag == true ? Result.ok().msg("增加一条开奖记录成功") : Result.error().msg("增加一条开奖记录失败");
    }

    /**
     * 获取开奖列表
     *
     * @return
     */
    @Operation(summary = "获取开奖列表")
    @PostMapping("/getLottery")
    public Result getLottery(@RequestBody LotteryQueryVO lotteryQueryVO) {
        //创建分页对象
        IPage<Lottery> page = new Page<Lottery>(1L, lotteryQueryVO.getPageSize());
        //调用分页查询方法
        lotteryService.findLotterys(page, lotteryQueryVO);

        return Result.ok(page);
    }

    /*获取数组里的偶数的个数*/
    public static Integer findEvenNum(char[] arr) {
        Integer EvenNum = 0;
        for (char i : arr) {
            if (Integer.parseInt(String.valueOf(i)) % 2 == 0) {
                EvenNum += 1;
            }
        }
        return EvenNum;
    }

    /*获取数组里的小于4的个数*/
    public static Integer findMinFour(char[] arr) {
        Integer minFour = 0;
        for (char i : arr) {
            if (Integer.parseInt(String.valueOf(i)) <= 4) {
                minFour += 1;
            }
        }
        return minFour;
    }

    /*获取数组里的大于4的个数*/
    public static Integer findMaxFour(char[] arr) {
        Integer maxFour = 0;
        for (char i : arr) {
            if (Integer.parseInt(String.valueOf(i)) > 4) {
                maxFour += 1;
            }
        }
        return maxFour;
    }

    /*获取数组里的奇数的个数*/
    public static Integer findOddNum(char[] arr) {
        Integer OddNum = 0;
        for (char i : arr) {
            if (Integer.parseInt(String.valueOf(i)) % 2 != 0) {
                OddNum += 1;
            }
        }
        return OddNum;
    }

    /*冒泡排序*/
    public static char[] bubbSort(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    char num = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = num;
                }
            }
        }
        return arr;
    }


    /*获取数组里的和值*/
    public static Integer findAddNum(char[] arr) {
        Integer addNum = 0;
        for (char i : arr) {
            addNum += Integer.parseInt(String.valueOf(i));
        }
        return addNum;
    }


    /*获取数组里的最大值和最小值*/
    public static char findMaxNum(char[] arr) {
        char max = arr[0];
        for (char i = 1; i <= arr.length - 1; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static char findMinNum(char[] arr) {
        char min = arr[0];
        for (int i = 1; i <= arr.length - 1; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;

    }

//    public static void main(String[] args) {
//        char[] arr = {'9', '4', '5'};
//        char[] chars = bubbSort(arr);
//        for (char i : chars) {
//            System.out.println(i);
//        }
//        System.out.println(findMinFour(arr));
//        System.out.println(findOddNum(arr));
//        System.out.println(findEvenNum(arr));
//        System.out.println(findAddNum(arr));
//        char maxNum = findMaxNum(arr);
//        char minNum = findMinNum(arr);
//        System.out.println(Integer.valueOf(maxNum-minNum));
//    }

}

