package team.buaa.questionnaire.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import team.buaa.questionnaire.entity.domain.Question;
import team.buaa.questionnaire.entity.domain.Questionnaire;
import team.buaa.questionnaire.entity.vo.QuestionIdTypeVO;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("select content from question ${ew.customSqlSegment}")
    List<String> getQuestionContentByQuestionnaireId(@Param(Constants.WRAPPER) QueryWrapper<Question> queryWrapper);

    @Select("select id, type from question ${ew.customSqlSegment}")
    List<QuestionIdTypeVO> getQuestionIdTypeByQuestionnaireId(@Param(Constants.WRAPPER) QueryWrapper<Question> queryWrapper);
}
