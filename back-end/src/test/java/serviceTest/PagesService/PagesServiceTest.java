package serviceTest.PagesService;// PagesServiceTest.java

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.entity.Pages;
import com.example.mapper.PagesMapper;
import com.example.service.PagesService;
import com.example.service.QuestionService;
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
public class PagesServiceTest {

    @Mock
    private PagesMapper pagesMapper;

    @InjectMocks
    private PagesService pagesService;
    @Mock
    private QuestionService questionService;

    private Pages page1;
    private Pages page2;

    @BeforeEach
    public void setUp() {
        page1 = new Pages();
        page1.setId(1);
        page1.setName("Page 1");
        page1.setCount(0);

        page2 = new Pages();
        page2.setId(2);
        page2.setName("Page 2");
    }

    @Test
    public void testAdd() {
        // 模拟 insert 方法返回的行为
        when(pagesMapper.insert(page1)).thenReturn(1);

        pagesService.add(page1);

        verify(pagesMapper, times(1)).insert(page1);
    }

    @Test
    public void testDeleteById() {
        // deleteById 是 void 方法，直接调用即可
        pagesService.deleteById(1);

        verify(pagesMapper, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2);

        pagesService.deleteBatch(ids);

        verify(pagesMapper, times(1)).deleteById(1);
        verify(pagesMapper, times(1)).deleteById(2);
    }

    @Test
    public void testUpdateById() {
        // 模拟 updateById 方法返回的行为
        when(pagesMapper.updateById(page1)).thenReturn(1);

        pagesService.updateById(page1);

        verify(pagesMapper, times(1)).updateById(page1);
    }

    @Test
    public void testSelectById() {
        // 模拟 selectById 方法返回的行为
        when(pagesMapper.selectById(1)).thenReturn(page1);

        Pages result = pagesService.selectById(1);

        assertNotNull(result);
        assertEquals("Page 1", result.getName());
        verify(pagesMapper, times(1)).selectById(1);
    }

    @Test
    public void testSelectAll() {
        // 模拟 selectAll 方法返回的行为
        List<Pages> pagesList = Arrays.asList(page1, page2);
        when(pagesMapper.selectAll(any(Pages.class))).thenReturn(pagesList);

        List<Pages> result = pagesService.selectAll(new Pages());

        assertEquals(2, result.size());
        assertEquals("Page 1", result.get(0).getName());
        verify(pagesMapper, times(1)).selectAll(any(Pages.class));
    }

    @Test
    public void testSelectPage() {
        // 模拟 selectAll 方法返回的行为以用于分页
        List<Pages> pagesList = Arrays.asList(page1, page2);
        when(pagesMapper.selectAll(any(Pages.class))).thenReturn(pagesList);

        PageInfo<Pages> result = pagesService.selectPage(new Pages(), 1, 10);

        assertEquals(2, result.getList().size());
        verify(pagesMapper, times(1)).selectAll(any(Pages.class));
    }
}
