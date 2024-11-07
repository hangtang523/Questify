package serviceTest.AdminService;

import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminMapper adminMapper;  // 模拟 adminMapper

    @InjectMocks
    private AdminService adminService;

    @Test
    void testAdd_AdminAlreadyExists() {
        // 模拟已存在用户名
        Admin existingAdmin = new Admin();
        existingAdmin.setUsername("admin");
        when(adminMapper.selectByUsername("admin")).thenReturn(existingAdmin);

        Admin newAdmin = new Admin();
        newAdmin.setUsername("admin");

        // 验证异常
        CustomException exception = assertThrows(CustomException.class, () -> adminService.add(newAdmin));
        assertEquals(ResultCodeEnum.USER_EXIST_ERROR.code, exception.getCode());

        // 验证没有执行插入操作
        verify(adminMapper, never()).insert(any(Admin.class));
    }

    @Test
    void testAdd_NewAdmin() {
        // 模拟用户名不存在
        when(adminMapper.selectByUsername("newAdmin")).thenReturn(null);

        Admin newAdmin = new Admin();
        newAdmin.setUsername("newAdmin");

        adminService.add(newAdmin);

        // 验证插入操作被调用
        verify(adminMapper, times(1)).insert(newAdmin);
        assertEquals(RoleEnum.ADMIN.name(), newAdmin.getRole());
        assertEquals(Constants.USER_DEFAULT_PASSWORD, newAdmin.getPassword());
    }

    @Test
    void testDeleteById() {
        adminService.deleteById(1);

        // 验证删除操作被调用
        verify(adminMapper, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2, 3);

        adminService.deleteBatch(ids);

        // 验证每个ID都调用了删除操作
        verify(adminMapper, times(1)).deleteById(1);
        verify(adminMapper, times(1)).deleteById(2);
        verify(adminMapper, times(1)).deleteById(3);
    }

    @Test
    void testSelectById() {
        Admin admin = new Admin();
        admin.setId(1);
        when(adminMapper.selectById(1)).thenReturn(admin);

        Admin result = adminService.selectById(1);

        // 验证返回结果正确
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testSelectAll() {
        List<Admin> admins = Arrays.asList(new Admin(), new Admin());
        when(adminMapper.selectAll(any(Admin.class))).thenReturn(admins);

        List<Admin> result = adminService.selectAll(new Admin());

        // 验证返回列表
        assertEquals(2, result.size());
    }

    @Test
    void testSelectPage() {
        List<Admin> admins = Arrays.asList(new Admin(), new Admin());
        when(adminMapper.selectAll(any(Admin.class))).thenReturn(admins);

        PageInfo<Admin> result = adminService.selectPage(new Admin(), 1, 10);

        // 验证分页结果
        assertEquals(2, result.getList().size());
    }

    @Test
    void testLogin_UserNotFound() {
        when(adminMapper.selectByUsername("admin")).thenReturn(null);

        Account account = new Account();
        account.setUsername("admin");

        // 验证异常
        CustomException exception = assertThrows(CustomException.class, () -> adminService.login(account));
        assertEquals(ResultCodeEnum.USER_NOT_EXIST_ERROR.code, exception.getCode());
    }

    @Test
    void testLogin_PasswordIncorrect() {
        Admin dbAccount = new Admin();
        dbAccount.setUsername("admin");
        dbAccount.setPassword("correctPassword");
        when(adminMapper.selectByUsername("admin")).thenReturn(dbAccount);

        Account account = new Account();
        account.setUsername("admin");
        account.setPassword("wrongPassword");

        // 验证异常
        CustomException exception = assertThrows(CustomException.class, () -> adminService.login(account));
        assertEquals(ResultCodeEnum.USER_ACCOUNT_ERROR.code, exception.getCode());
    }

    @Test
    void testLogin_Success() {
        Admin dbAccount = new Admin();
        dbAccount.setUsername("admin");
        dbAccount.setPassword("correctPassword");
        when(adminMapper.selectByUsername("admin")).thenReturn(dbAccount);

        Account account = new Account();
        account.setUsername("admin");
        account.setPassword("correctPassword");

        Account result = adminService.login(account);

        // 验证token生成
        assertNotNull(result.getToken());
        assertEquals("admin", result.getUsername());
    }

    @Test
    void testRegister() {
        Account account = new Account();
        account.setUsername("newAdmin");
        account.setPassword("password");

        adminService.register(account);

        // 验证插入操作
        verify(adminMapper, times(1)).insert(any(Admin.class));
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        when(adminMapper.selectByUsername("admin")).thenReturn(null);

        Account account = new Account();
        account.setUsername("admin");

        // 验证异常
        CustomException exception = assertThrows(CustomException.class, () -> adminService.updatePassword(account));
        assertEquals(ResultCodeEnum.USER_NOT_EXIST_ERROR.code, exception.getCode());
    }

    @Test
    void testUpdatePassword_Success() {
        Admin dbAdmin = new Admin();
        dbAdmin.setUsername("admin");
        dbAdmin.setPassword("oldPassword");
        when(adminMapper.selectByUsername("admin")).thenReturn(dbAdmin);

        Account account = new Account();
        account.setUsername("admin");
        account.setPassword("oldPassword");
        account.setNewPassword("newPassword");

        adminService.updatePassword(account);

        // 验证更新操作
        assertEquals("newPassword", dbAdmin.getPassword());
        verify(adminMapper, times(1)).updateById(dbAdmin);
    }


}