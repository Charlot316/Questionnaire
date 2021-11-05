package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamAnswerResultVO {
    private Integer questionId;
    private Integer type;
    private Integer max; // 多选题空格数
    private Double score; // 总分
    private Double realScore; // 实际得分
    private Integer isCorrect; // 0-未评分 1-正确 2-错误
    private List<String> correctAnswer = new ArrayList<>();
    private List<String> trueAnswer = new ArrayList<>();
}
