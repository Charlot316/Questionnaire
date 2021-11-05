package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChoiceDTO {
    private Integer choiceId;
    private String choiceContent;
    private Integer isLimit;
    private Integer allCount;
    private Integer isAnswer;
}
