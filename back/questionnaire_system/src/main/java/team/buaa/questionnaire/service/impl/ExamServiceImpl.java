package team.buaa.questionnaire.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.buaa.questionnaire.entity.domain.Exam;
import team.buaa.questionnaire.mapper.ExamMapper;
import team.buaa.questionnaire.service.IExamService;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements IExamService {

}
