package team.buaa.questionnaire.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import team.buaa.questionnaire.entity.domain.Choice;
import team.buaa.questionnaire.entity.domain.Questionnaire;
import team.buaa.questionnaire.entity.domain.Score;
import team.buaa.questionnaire.entity.vo.ChoiceVO;
import team.buaa.questionnaire.entity.vo.ScoreVO;

import java.util.List;

@Mapper
public interface ScoreMapper extends BaseMapper<Score> {

    @Select("select score_content from score ${ew.customSqlSegment}")
    List<String> getScoreContents(@Param(Constants.WRAPPER) QueryWrapper<Score> queryWrapper);

    @Select("select score_id, score_content from score ${ew.customSqlSegment}")
    List<ScoreVO> getScoreByQuestionId(@Param(Constants.WRAPPER) QueryWrapper<Score> queryWrapper);
}
