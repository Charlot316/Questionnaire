package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnswerAnalysisVO {
    // 对于 0-单选 1-多选 2-评分 保存各选项的填写人数和百分数
    // 对于 3-单项填空 4-多项填空 保存所有答案
    ExamAnalysisVO examAnalysisVO;
    private List<QuestionContentVO> questionContentVOS = new ArrayList<>();
    private List<AnswerResultVO> answerResultVOS = new ArrayList<>();
}
