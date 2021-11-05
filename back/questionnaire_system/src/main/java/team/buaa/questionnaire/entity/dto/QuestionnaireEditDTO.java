package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionnaireEditDTO {
    private Integer id; // 问卷id
    private Integer userId;
    private String name;
    private String introduction;
    private Integer count;
    private Integer isStar;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer isRepeat;
    private Integer isLogin;
    private Integer isAppoint;
    private Integer isNumber;
    private Integer type;
    private Integer isLimit;
    private Integer allCount;
    private Integer isOrder;
    private List<QuestionDTO> questions;
}
