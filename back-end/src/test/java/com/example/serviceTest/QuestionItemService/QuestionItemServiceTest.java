package com.example.serviceTest.QuestionItemService;// QuestionItemServiceTest.java

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.entity.QuestionItem;
import com.example.mapper.QuestionItemMapper;
import com.example.service.QuestionItemService;
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
public class QuestionItemServiceTest {

    @Mock
    private QuestionItemMapper questionItemMapper;

    @InjectMocks
    private QuestionItemService questionItemService;

    private QuestionItem questionItem1;
    private QuestionItem questionItem2;

    @BeforeEach
    public void setUp() {
        questionItem1 = new QuestionItem();
        questionItem1.setId(1);
        questionItem1.setContent("Item 1 Content");

        questionItem2 = new QuestionItem();
        questionItem2.setId(2);
        questionItem2.setContent("Item 2 Content");
    }

    @Test
    public void testAdd() {
        // 假设 insert 返回 int 类型
        when(questionItemMapper.insert(questionItem1)).thenReturn(1);

        questionItemService.add(questionItem1);

        verify(questionItemMapper, times(1)).insert(questionItem1);
    }

    @Test
    public void testDeleteById() {
        // deleteById 是 void 方法，不需要 doNothing()
        questionItemService.deleteById(1);

        verify(questionItemMapper, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2);

        questionItemService.deleteBatch(ids);

        verify(questionItemMapper, times(1)).deleteById(1);
        verify(questionItemMapper, times(1)).deleteById(2);
    }

    @Test
    public void testUpdateById() {
        // 假设 updateById 返回 int 类型
        when(questionItemMapper.updateById(questionItem1)).thenReturn(1);

        questionItemService.updateById(questionItem1);

        verify(questionItemMapper, times(1)).updateById(questionItem1);
    }

    @Test
    public void testSelectById() {
        when(questionItemMapper.selectById(1)).thenReturn(questionItem1);

        QuestionItem result = questionItemService.selectById(1);

        assertNotNull(result);
        assertEquals("Item 1 Content", result.getContent());
        verify(questionItemMapper, times(1)).selectById(1);
    }

    @Test
    public void testSelectAll() {
        List<QuestionItem> itemList = Arrays.asList(questionItem1, questionItem2);
        when(questionItemMapper.selectAll(any(QuestionItem.class))).thenReturn(itemList);

        List<QuestionItem> result = questionItemService.selectAll(new QuestionItem());

        assertEquals(2, result.size());
        assertEquals("Item 1 Content", result.get(0).getContent());
        verify(questionItemMapper, times(1)).selectAll(any(QuestionItem.class));
    }

    @Test
    public void testSelectPage() {
        List<QuestionItem> itemList = Arrays.asList(questionItem1, questionItem2);
        when(questionItemMapper.selectAll(any(QuestionItem.class))).thenReturn(itemList);

        PageInfo<QuestionItem> result = questionItemService.selectPage(new QuestionItem(), 1, 10);

        assertEquals(2, result.getList().size());
        verify(questionItemMapper, times(1)).selectAll(any(QuestionItem.class));
    }
    @Test
    public void testSelectByQuestionId() {
        List<QuestionItem> itemList = Arrays.asList(questionItem1, questionItem2);
        when(questionItemMapper.selectByQuestionId(1)).thenReturn(itemList);

        List<QuestionItem> result = questionItemService.selectByQuestionId(1);

        assertEquals(2, result.size());
        verify(questionItemMapper, times(1)).selectByQuestionId(1);
    }

    @Test
    public void testDeleteByQuestionId() {
        // deleteByQuestionId 是 void 方法，不需要 doNothing()
        questionItemService.deleteByQuestionId(1);

        verify(questionItemMapper, times(1)).deleteByQuestionId(1);
    }
}
