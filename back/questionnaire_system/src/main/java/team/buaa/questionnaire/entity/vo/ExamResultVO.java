package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamResultVO {
    private Double score; // 客观题总分
    private Double allScore; // 总分（包括不评分的题）
    private Integer correctCount; // 正确题目数量
    private List<QuestionContentVO> questionContentVOS = new ArrayList<>();
    private List<ExamAnswerResultVO> examAnswerResultVOS = new ArrayList<>();
}
