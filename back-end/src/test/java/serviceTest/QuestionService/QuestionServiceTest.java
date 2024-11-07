package serviceTest.QuestionService;// QuestionServiceTest.java

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.entity.Question;
import com.example.mapper.QuestionMapper;
import com.example.service.QuestionService;
import com.example.service.QuestionItemService;
import com.github.pagehelper.PageInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private QuestionItemService questionItemService;

    @InjectMocks
    private QuestionService questionService;

    private Question question1;
    private Question question2;

    @BeforeEach
    public void setUp() {
        question1 = new Question();
        question1.setId(1);
        question1.setName("Question 1");

        question2 = new Question();
        question2.setId(2);
        question2.setName("Question 2");

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testAdd() {
        // 假设 insert 返回 int 类型
        when(questionMapper.insert(question1)).thenReturn(1);

        questionService.add(question1);

        verify(questionMapper, times(1)).insert(question1);
    }

    @Test
    public void testUpdateById() {
        // 假设 updateById 返回 int 类型
        when(questionMapper.updateById(question1)).thenReturn(1);

        questionService.updateById(question1);

        verify(questionMapper, times(1)).updateById(question1);
    }

    @Test
    public void testSelectById() {
        when(questionMapper.selectById(1)).thenReturn(question1);

        Question result = questionService.selectById(1);

        assertNotNull(result);
        assertEquals("Question 1", result.getName());
        verify(questionMapper, times(1)).selectById(1);
    }

    @Test
    public void testSelectAll() {
        List<Question> questionList = Arrays.asList(question1, question2);
        when(questionMapper.selectAll(any(Question.class))).thenReturn(questionList);

        List<Question> result = questionService.selectAll(new Question());

        assertEquals(2, result.size());
        assertEquals("Question 1", result.get(0).getName());
        verify(questionMapper, times(1)).selectAll(any(Question.class));
    }

    @Test
    public void testSelectPage() {
        List<Question> questionList = Arrays.asList(question1, question2);
        when(questionMapper.selectAll(any(Question.class))).thenReturn(questionList);

        PageInfo<Question> result = questionService.selectPage(new Question(), 1, 10);

        assertEquals(2, result.getList().size());
        verify(questionMapper, times(1)).selectAll(any(Question.class));
    }
}
