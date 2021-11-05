package team.buaa.questionnaire.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Score {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer questionId;
    private Integer scoreId;
    private String scoreContent;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
