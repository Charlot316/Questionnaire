package team.buaa.questionnaire.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionnaireInfVO {
    private Integer id;
    private String name;
    private Integer count;
    private Integer isStar; // 0-未收藏 1-收藏
    private Integer status; // 0-保存 1-发布  2-暂停 3-已删除
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy年MM月dd日 HH:mm")
    private LocalDateTime updateTime;
    private Integer questionnaireType;
}
