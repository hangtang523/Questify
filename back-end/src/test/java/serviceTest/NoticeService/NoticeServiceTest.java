package serviceTest.NoticeService;

import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeService noticeService;

    private Notice notice1;
    private Notice notice2;

    @BeforeEach
    public void setUp() {
        notice1 = new Notice();
        notice1.setId(1);
        notice1.setTitle("Notice 1");
        notice1.setContent("Content for notice 1");

        notice2 = new Notice();
        notice2.setId(2);
        notice2.setTitle("Notice 2");
        notice2.setContent("Content for notice 2");
    }

    @Test
    public void testAdd() {
        // 使用 when().then() 来模拟方法调用
        when(noticeMapper.insert(notice1)).thenReturn(1); // 假设 insert 返回 int

        noticeService.add(notice1);

        verify(noticeMapper, times(1)).insert(notice1);
    }

    @Test
    public void testDeleteById() {
        // deleteById 是 void 方法，可以直接调用 verify
        noticeService.deleteById(1);

        verify(noticeMapper, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2);

        // 批量删除调用 deleteById，直接验证调用次数
        noticeService.deleteBatch(ids);

        verify(noticeMapper, times(1)).deleteById(1);
        verify(noticeMapper, times(1)).deleteById(2);
    }

    @Test
    public void testUpdateById() {
        // 使用 when().then() 来模拟方法调用
        when(noticeMapper.updateById(notice1)).thenReturn(1); // 假设 updateById 返回 int

        noticeService.updateById(notice1);

        verify(noticeMapper, times(1)).updateById(notice1);
    }

    @Test
    public void testSelectById() {
        when(noticeMapper.selectById(1)).thenReturn(notice1);

        Notice result = noticeService.selectById(1);

        assertNotNull(result);
        assertEquals("Notice 1", result.getTitle());
        verify(noticeMapper, times(1)).selectById(1);
    }

    @Test
    public void testSelectAll() {
        List<Notice> notices = Arrays.asList(notice1, notice2);
        when(noticeMapper.selectAll(any(Notice.class))).thenReturn(notices);

        List<Notice> result = noticeService.selectAll(new Notice());

        assertEquals(2, result.size());
        assertEquals("Notice 1", result.get(0).getTitle());
        verify(noticeMapper, times(1)).selectAll(any(Notice.class));
    }

    @Test
    public void testSelectPage() {
        List<Notice> notices = Arrays.asList(notice1, notice2);
        when(noticeMapper.selectAll(any(Notice.class))).thenReturn(notices);

        PageInfo<Notice> result = noticeService.selectPage(new Notice(), 1, 10);

        assertEquals(2, result.getList().size());
        verify(noticeMapper, times(1)).selectAll(any(Notice.class));
    }
}
