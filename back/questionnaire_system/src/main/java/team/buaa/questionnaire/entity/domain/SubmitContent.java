package team.buaa.questionnaire.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmitContent {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer submitRecordId;
    private Integer questionId;
    private String content;
    private Double score; // 考试每道题实际得分

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public SubmitContent(Integer submitRecordId, Integer questionId, String content) {
        this.submitRecordId = submitRecordId;
        this.questionId = questionId;
        this.content = content;
    }

    public SubmitContent(Integer submitRecordId, Integer questionId, String content, Double score) {
        this.submitRecordId = submitRecordId;
        this.questionId = questionId;
        this.content = content;
        this.score = score;
    }

    public SubmitContent() {}
}
