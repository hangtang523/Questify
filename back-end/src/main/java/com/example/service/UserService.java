package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);  // 设置默认密码
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectAll(user);
        return PageInfo.of(userList);
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }
    /*用户注册*/
    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);
        this.add(user);
    }
    /*修改密码*/
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

}