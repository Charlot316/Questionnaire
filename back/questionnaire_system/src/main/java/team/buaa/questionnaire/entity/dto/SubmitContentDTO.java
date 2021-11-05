package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitContentDTO {
    private Integer userId;
    private Integer questionnaireId;
    private String ip;
    private List<QuestionSubmitDTO> questionSubmitDTOS = new ArrayList<>();
}
