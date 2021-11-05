package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckDTO {
    private Integer questionnaireId;
    private String code;
    private Integer isLogin;
    private Integer userId;
    private String ip;
}
