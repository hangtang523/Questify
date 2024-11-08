package com.example.serviceTest.AnswerService;// AnswerServiceTest.java

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.entity.Answer;
import com.example.mapper.AnswerMapper;
import com.example.service.AnswerService;
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
public class AnswerServiceTest {

    @Mock
    private AnswerMapper answerMapper;

    @InjectMocks
    private AnswerService answerService;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    public void setUp() {
        answer1 = new Answer();
        answer1.setId(1);
        answer1.setContent("Answer 1 Content");

        answer2 = new Answer();
        answer2.setId(2);
        answer2.setContent("Answer 2 Content");
    }

    @Test
    public void testAdd() {
        // 使用 when().thenReturn() 来模拟返回值
        when(answerMapper.insert(answer1)).thenReturn(1); // 假设 insert 返回 int

        answerService.add(answer1);

        verify(answerMapper, times(1)).insert(answer1);
    }

    @Test
    public void testDeleteById() {
        // deleteById 是 void 方法，可以直接验证调用
        answerService.deleteById(1);

        verify(answerMapper, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteBatch() {
        List<Integer> ids = Arrays.asList(1, 2);

        answerService.deleteBatch(ids);

        verify(answerMapper, times(1)).deleteById(1);
        verify(answerMapper, times(1)).deleteById(2);
    }

    @Test
    public void testUpdateById() {
        // 使用 when().thenReturn() 来模拟返回值
        when(answerMapper.updateById(answer1)).thenReturn(1); // 假设 updateById 返回 int

        answerService.updateById(answer1);

        verify(answerMapper, times(1)).updateById(answer1);
    }

    @Test
    public void testSelectById() {
        when(answerMapper.selectById(1)).thenReturn(answer1);

        Answer result = answerService.selectById(1);

        assertNotNull(result);
        assertEquals("Answer 1 Content", result.getContent());
        verify(answerMapper, times(1)).selectById(1);
    }

    @Test
    public void testSelectAll() {
        List<Answer> answerList = Arrays.asList(answer1, answer2);
        when(answerMapper.selectAll(any(Answer.class))).thenReturn(answerList);

        List<Answer> result = answerService.selectAll(new Answer());

        assertEquals(2, result.size());
        assertEquals("Answer 1 Content", result.get(0).getContent());
        verify(answerMapper, times(1)).selectAll(any(Answer.class));
    }

    @Test
    public void testSelectPage() {
        List<Answer> answerList = Arrays.asList(answer1, answer2);
        when(answerMapper.selectAll(any(Answer.class))).thenReturn(answerList);

        PageInfo<Answer> result = answerService.selectPage(new Answer(), 1, 10);

        assertEquals(2, result.getList().size());
        verify(answerMapper, times(1)).selectAll(any(Answer.class));
    }
    @Test
    public void testAddBatch() {
        List<Answer> answerList = Arrays.asList(answer1, answer2);

        answerService.addBatch(answerList);

        verify(answerMapper, times(1)).insert(answer1);
        verify(answerMapper, times(1)).insert(answer2);
    }
    @Test
    public void testSelectByPageId() {
        List<Answer> answerList = Arrays.asList(answer1, answer2);
        when(answerMapper.selectByPageId(1)).thenReturn(answerList);

        List<Answer> result = answerService.selectByPageId(1);

        assertEquals(2, result.size());
        verify(answerMapper, times(1)).selectByPageId(1);
    }
}
