package team.buaa.questionnaire.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;
    private String password;
    private Integer sex; // 0-男 1-女
    private String email;
    private String phoneNumber;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public User(String username, String password, int sex, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User() {

    }
}
