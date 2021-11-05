package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnswerResultVO {
    // 对于 0-单选 1-多选 2-评分 保存各选项的填写人数和百分数
    // 对于 3-单项填空 4-多项填空 保存所有答案
    private Integer questionId;
    private Integer type;
    private Integer sum; // 有效作答人数
    private String correctRate; // 正确率
    private List<ChoiceAnswerVO> choiceAnswerVOS = new ArrayList<>();
    private List<ScoreAnswerVO> scoreAnswerVOS = new ArrayList<>();
//    private List<List<String>> content = new ArrayList<>();
}
