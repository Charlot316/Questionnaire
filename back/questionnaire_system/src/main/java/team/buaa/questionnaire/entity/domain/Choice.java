package team.buaa.questionnaire.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
public class Choice {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer questionId;
    private Integer choiceId;
    private String choiceContent;
    private Integer allCount;
    private Integer nowCount;
    private Integer isLimit;
    private Integer isAnswer; // 0-不是正确答案 1-是正确答案

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Choice() {}
}
