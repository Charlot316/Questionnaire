package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnsweringVO {
    private List<QuestionContentVO> questionContentVOS = new ArrayList<>();
    private List<UserAnsweringVO> userAnsweringVOS = new ArrayList<>();

}
