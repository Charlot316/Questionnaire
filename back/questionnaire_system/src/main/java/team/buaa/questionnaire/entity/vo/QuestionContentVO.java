package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionContentVO {
    private Integer id;
    private String content;
    private List<String> correctAnswer = new ArrayList<>();
}
