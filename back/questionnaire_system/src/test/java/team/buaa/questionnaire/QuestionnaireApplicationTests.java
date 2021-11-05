package team.buaa.questionnaire;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itextpdf.text.pdf.BaseFont;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import team.buaa.questionnaire.controller.questionnaire.QuestionnaireController;
import team.buaa.questionnaire.entity.domain.*;
import team.buaa.questionnaire.mapper.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionnaireApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionnaireMapper questionnaireMapper;
    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private SubmitContentMapper submitContentMapper;
    @Autowired
    private SubmitRecordMapper submitRecordMapper;

    @Test
    void test() {
        User user = userMapper.selectById(17);
        user.setPassword("123456");
        userMapper.updateById(user);
    }

}
