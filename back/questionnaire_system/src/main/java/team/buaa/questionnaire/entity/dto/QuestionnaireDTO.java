package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionnaireDTO {
    private Integer userId;
    private String name;
    private String introduction;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer isRepeat;
    private Integer isLogin;
    private Integer isAppoint;
    private Integer type;
    private Integer isNumber;
    private Integer allCount;
    private Integer isLimit;
    private Integer isOrder;
    private List<QuestionDTO> questions;
}
