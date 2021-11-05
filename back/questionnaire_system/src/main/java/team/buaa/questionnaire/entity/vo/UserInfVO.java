package team.buaa.questionnaire.entity.vo;

import lombok.Data;

@Data
public class UserInfVO {
    private String username;
    private String password;
    private Integer sex;
    private String email;
    private String phoneNumber;
}
