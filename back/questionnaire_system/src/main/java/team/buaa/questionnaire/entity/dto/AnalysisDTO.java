package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisDTO {
    private Integer independentVariableId;
    private Integer dependentVariableId;
    private Integer userId;
}