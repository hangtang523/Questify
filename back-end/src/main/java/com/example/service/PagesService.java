package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Pages;
import com.example.mapper.PagesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 问卷表业务处理
 **/
@Service
public class PagesService {

    @Resource
    private PagesMapper pagesMapper;


    /**
     * 新增
     */

    /**
     * 新增
     */
    public void add(Pages pages) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            pages.setUserId(currentUser.getId());
        }
        pages.setCreateTime(DateUtil.now());
        pagesMapper.insert(pages);
    }

    /**
     * 删除
     */
    @Transactional
    public void deleteById(Integer id) {
        pagesMapper.deleteById(id);
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
    public void updateById(Pages pages) {
        pagesMapper.updateById(pages);
    }

    /**
     * 根据ID查询
     */
    public Pages selectById(Integer id) {
        return pagesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Pages> selectAll(Pages pages) {
        return pagesMapper.selectAll(pages);
    }

    /**
     * 分页查询
     */
    public PageInfo<Pages> selectPage(Pages pages, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Pages> list = pagesMapper.selectAll(pages);
        return PageInfo.of(list);
    }

}