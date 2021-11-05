package team.buaa.questionnaire.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.buaa.questionnaire.entity.domain.Choice;
import team.buaa.questionnaire.entity.domain.Question;
import team.buaa.questionnaire.entity.vo.ChoiceVO;
import team.buaa.questionnaire.entity.vo.QuestionIdTypeVO;

import java.util.List;

@Mapper
public interface ChoiceMapper extends BaseMapper<Choice> {

    @Select("select choice_id, choice_content from choice ${ew.customSqlSegment}")
    List<ChoiceVO> getChoiceByQuestionId(@Param(Constants.WRAPPER) QueryWrapper<Choice> queryWrapper);
}
