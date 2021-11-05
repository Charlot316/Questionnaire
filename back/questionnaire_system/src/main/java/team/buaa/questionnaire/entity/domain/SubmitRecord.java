package team.buaa.questionnaire.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmitRecord {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer questionnaireId;
    private Integer userId;
    private LocalDateTime submitTime;
    private String ip;
    private Double score; // 客观题总分

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public SubmitRecord(Integer questionnaireId, Integer userId, LocalDateTime submitTime, String ip) {
        this.questionnaireId = questionnaireId;
        this.userId = userId;
        this.submitTime = submitTime;
        this.ip = ip;
    }

    public SubmitRecord() {}
}
