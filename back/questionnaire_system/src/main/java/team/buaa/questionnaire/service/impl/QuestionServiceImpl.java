package team.buaa.questionnaire.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.buaa.questionnaire.entity.domain.Question;
import team.buaa.questionnaire.mapper.QuestionMapper;
import team.buaa.questionnaire.service.IQuestionService;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
}
