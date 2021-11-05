package team.buaa.questionnaire.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.buaa.questionnaire.entity.domain.Choice;
import team.buaa.questionnaire.mapper.ChoiceMapper;
import team.buaa.questionnaire.service.IChoiceService;

@Service
public class ChoiceServiceImpl extends ServiceImpl<ChoiceMapper, Choice> implements IChoiceService {
}
