package team.buaa.questionnaire.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.buaa.questionnaire.entity.domain.Questionnaire;
import team.buaa.questionnaire.entity.domain.SubmitContent;
import team.buaa.questionnaire.entity.vo.ContentUpdateTimeVO;
import team.buaa.questionnaire.entity.vo.QuestionAnsweringVO;

import java.util.List;

@Mapper
public interface SubmitContentMapper extends BaseMapper<SubmitContent> {

    @Select("select content from submit_content ${ew.customSqlSegment}")
    List<String> getAnsweringContentByRecordId(@Param(Constants.WRAPPER) QueryWrapper<SubmitContent> queryWrapper);

    @Select("select question_id, type, submit_content.content as content from submit_content join question on submit_content.question_id = question.id" +
            " ${ew.customSqlSegment} order by question_id")
    List<QuestionAnsweringVO> getAnswerQuestionContentByRecordId(@Param(Constants.WRAPPER) QueryWrapper<SubmitContent> queryWrapper);

    @Select("select content, update_time from submit_content ${ew.customSqlSegment}")
    List<ContentUpdateTimeVO> getContentTimeByQuestionId(@Param(Constants.WRAPPER) QueryWrapper<SubmitContent> queryWrapper);

    @Select("select count(*) as count from submit_content A join submit_content B on A.submit_record_id = B.submit_record_id ${ew.customSqlSegment}")
    Integer getCountByCrossAnalysis(@Param(Constants.WRAPPER) QueryWrapper<SubmitContent> queryWrapper);


}
