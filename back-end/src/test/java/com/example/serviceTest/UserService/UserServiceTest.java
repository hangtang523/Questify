package com.example.serviceTest.UserService;// UserServiceTest.java

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.entity.User;
import com.example.entity.Account;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.exception.CustomException;
import com.example.common.enums.ResultCodeEnum;
import com.github.pagehelper.PageInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");

        user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
    }

    @Test
    public void testSelectAll() {
        List<User> userList = Arrays.asList(user1, user2);
        when(userMapper.selectAll(any(User.class))).thenReturn(userList);

        List<User> result = userService.selectAll(new User());

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(userMapper, times(1)).selectAll(any(User.class));
    }

    @Test
    public void testAdd_UserAlreadyExists() {
        when(userMapper.selectByUsername("user1")).thenReturn(user1);

        User newUser = new User();
        newUser.setUsername("user1");

        CustomException exception = assertThrows(CustomException.class, () -> userService.add(newUser));
        assertEquals(ResultCodeEnum.USER_EXIST_ERROR.getCode(), exception.getCode());

        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    public void testAdd_NewUser() {
        when(userMapper.selectByUsername("user3")).thenReturn(null);

        User newUser = new User();
        newUser.setUsername("user3");

        userService.add(newUser);

        verify(userMapper, times(1)).insert(newUser);
    }

    @Test
    public void testDeleteById() {
        doNothing().when(userMapper).deleteById(1);

        userService.deleteById(1);

        verify(userMapper, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2);
        doNothing().when(userMapper).deleteById(anyInt());

        userService.deleteBatch(ids);

        verify(userMapper, times(1)).deleteById(1);
        verify(userMapper, times(1)).deleteById(2);
    }

    @Test
    public void testSelectById() {
        when(userMapper.selectById(1)).thenReturn(user1);

        User result = userService.selectById(1);

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        verify(userMapper, times(1)).selectById(1);
    }

    @Test
    public void testSelectPage() {
        List<User> userList = Arrays.asList(user1, user2);
        when(userMapper.selectAll(any(User.class))).thenReturn(userList);

        PageInfo<User> result = userService.selectPage(new User(), 1, 10);

        assertEquals(2, result.getList().size());
        verify(userMapper, times(1)).selectAll(any(User.class));
    }

    @Test
    public void testUpdateById() {
        doNothing().when(userMapper).updateById(user1);

        userService.updateById(user1);

        verify(userMapper, times(1)).updateById(user1);
    }

    @Test
    public void testLogin_UserNotFound() {
        when(userMapper.selectByUsername("user1")).thenReturn(null);

        Account account = new Account();
        account.setUsername("user1");

        CustomException exception = assertThrows(CustomException.class, () -> userService.login(account));
        assertEquals(ResultCodeEnum.USER_NOT_EXIST_ERROR.getCode(), exception.getCode());
    }

    @Test
    public void testLogin_PasswordIncorrect() {
        User dbUser = new User();
        dbUser.setUsername("user1");
        dbUser.setPassword("correctPassword");
        when(userMapper.selectByUsername("user1")).thenReturn(dbUser);

        Account account = new Account();
        account.setUsername("user1");
        account.setPassword("wrongPassword");

        CustomException exception = assertThrows(CustomException.class, () -> userService.login(account));
        assertEquals(ResultCodeEnum.USER_ACCOUNT_ERROR.getCode(), exception.getCode());
    }

    @Test
    public void testRegister() {
        Account account = new Account();
        account.setUsername("newUser");

        userService.register(account);

        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    public void testUpdatePassword_UserNotFound() {
        when(userMapper.selectByUsername("user1")).thenReturn(null);

        Account account = new Account();
        account.setUsername("user1");

        CustomException exception = assertThrows(CustomException.class, () -> userService.updatePassword(account));
        assertEquals(ResultCodeEnum.USER_NOT_EXIST_ERROR.getCode(), exception.getCode());
    }

    @Test
    public void testUpdatePassword_Success() {
        User dbUser = new User();
        dbUser.setUsername("user1");
        dbUser.setPassword("oldPassword");
        when(userMapper.selectByUsername("user1")).thenReturn(dbUser);

        Account account = new Account();
        account.setUsername("user1");
        account.setPassword("oldPassword");
        account.setNewPassword("newPassword");

        userService.updatePassword(account);

        assertEquals("newPassword", dbUser.getPassword());
        verify(userMapper, times(1)).updateById(dbUser);
    }
}
