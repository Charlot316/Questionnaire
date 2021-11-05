package team.buaa.questionnaire.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.buaa.questionnaire.entity.domain.Score;
import team.buaa.questionnaire.mapper.ScoreMapper;
import team.buaa.questionnaire.service.IScoreService;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements IScoreService {
}
