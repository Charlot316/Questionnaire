package team.buaa.questionnaire.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.buaa.questionnaire.entity.domain.Questionnaire;
import team.buaa.questionnaire.entity.vo.QuestionnaireInfVO;

import java.util.List;

@Mapper
public interface QuestionnaireMapper extends BaseMapper<Questionnaire> {

    @Select("select id, name, count, is_star as isStar, status, update_time as updateTime, type as questionnaireType from questionnaire ${ew.customSqlSegment}")
    Page<QuestionnaireInfVO> getQuestionnaireInf(Page<QuestionnaireInfVO> page, @Param(Constants.WRAPPER) QueryWrapper<Questionnaire> queryWrapper);
}
