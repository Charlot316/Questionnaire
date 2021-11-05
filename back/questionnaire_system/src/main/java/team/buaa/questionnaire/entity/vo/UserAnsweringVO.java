package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserAnsweringVO {
    private String username;
    private LocalDateTime time;
    private List<AnswerContentVO> answerContentVOS = new ArrayList<>();
}
