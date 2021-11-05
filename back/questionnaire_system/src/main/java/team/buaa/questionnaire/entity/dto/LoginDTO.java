package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDTO {
    private String username;
    private String password;
    private Integer isAutoLogin; // 0-不自动登录 1-自动登录
}
