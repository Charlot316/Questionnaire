package team.buaa.questionnaire.entity.vo;

import lombok.Data;
import team.buaa.questionnaire.entity.dto.QuestionDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionnaireVO {
    private String name;
    private String introduction;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer isRepeat;
    private Integer isLogin;
    private Integer isAppoint;
    private Integer isNumber;
    private Integer type;
    private Integer allCount;
    private Integer isLimit;
    private Integer isOrder;
    private List<QuestionVO> questions = new ArrayList<>();
}
