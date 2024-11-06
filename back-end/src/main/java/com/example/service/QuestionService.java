package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Question;
import com.example.mapper.QuestionMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目表业务处理
 **/
@Service
public class QuestionService {

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 新增
     */
    /**
     * 新增
     */
    public void add(Question question) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            question.setUserId(currentUser.getId());
        }
        questionMapper.insert(question);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        questionMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Question question) {
        questionMapper.updateById(question);
    }

    /**
     * 根据ID查询
     */
    public Question selectById(Integer id) {
        return questionMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Question> selectAll(Question question) {
        return questionMapper.selectAll(question);
    }

    /**
     * 分页查询
     */
    public PageInfo<Question> selectPage(Question question, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectAll(question);
        return PageInfo.of(list);
    }

}