package team.buaa.questionnaire.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Question {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer questionnaireId;
    // 0-单选 1-多选 2-评分 3-单项填空 4-多项填空 5-投票单选 6-投票多选 7-定位
    private Integer type;
    private String content;
    private Integer isCompulsory; // 0-必填 1-选填
    private String introduction;
    private Integer maxScore;
    private Integer isShowVotes;
    private Integer isShowPercentage;
    private Double score; // 题目分数
    private String answer; // 题目答案
    private String analysis; // 题目解析

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Question(Integer questionnaireId, Integer type, String content, Integer isCompulsory, String introduction, Integer maxScore) {
        this.questionnaireId = questionnaireId;
        this.type = type;
        this.content = content;
        this.isCompulsory = isCompulsory;
        this.introduction = introduction;
        this.maxScore = maxScore;
    }

    public Question() {}
}
