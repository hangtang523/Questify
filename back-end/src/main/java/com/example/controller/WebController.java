package com.example.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.QuestionTypeEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Answer;
import com.example.entity.Question;
import com.example.entity.QuestionItem;
import com.example.service.AdminService;
import com.example.service.AnswerService;
import com.example.service.QuestionService;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础前端接口
 */
@RestController
@Api(tags = "Fundamental")
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    AnswerService answerService;
    @Resource
    QuestionService questionService;
    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @ApiOperation("Login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            account = userService.login(account);
        }
        return Result.success(account);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @ApiOperation("Register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.register(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    @ApiOperation("Update Password")
    public Result updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if(RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }
    /**
     * 数据统计
     * @return
     */
    @GetMapping("/pageCount")
    public Result pageCount(@RequestParam Integer pageId) {
        Dict pageCount = Dict.create();
        List<Answer> answerList = answerService.selectByPageId(pageId);
        long answerCount = answerList.stream().map(Answer::getNo).distinct().count();
        pageCount.set("answerCount", answerCount);

        List<Question> questionList = questionService.selectByPageId(pageId);
        for (Question question : questionList) {
            List<String> questionAnswerList = answerList.stream().filter(answer -> answer.getQuestionId().equals(question.getId()))
                    .map(Answer::getContent).collect(Collectors.toList());   // 用户提交的所有回答
            question.setCount(questionAnswerList.size());
            if (QuestionTypeEnum.SINGLE.getValue().equals(question.getType()) || QuestionTypeEnum.MULTIPLE.getValue().equals(question.getType())) {
                List<QuestionItem> questionItemList = question.getQuestionItemList();
                for (QuestionItem questionItem : questionItemList) {
                    long count = questionAnswerList.stream().filter(content -> content.contains(questionItem.getContent())).count();  // 统计当前的这个选项有多少回答
                    questionItem.setCount(count);
                    int percent = BigDecimal.valueOf(count).divide(BigDecimal.valueOf(questionAnswerList.size()), 2, RoundingMode.HALF_UP)   // 四舍五入
                            .multiply(BigDecimal.valueOf(100)).intValue();// 0.89 -> 89
                    questionItem.setPercentage(percent);
                }
            } else {
                List<QuestionItem> questionItemList = questionAnswerList.stream().map(content -> {
                    QuestionItem questionItem = new QuestionItem();
                    questionItem.setContent(content);
                    return questionItem;
                }).collect(Collectors.toList());
                question.setQuestionItemList(questionItemList);  // 设置填空题的选项集合
            }
        }

        pageCount.set("questionList", questionList);
        return Result.success(pageCount);
    }

}


