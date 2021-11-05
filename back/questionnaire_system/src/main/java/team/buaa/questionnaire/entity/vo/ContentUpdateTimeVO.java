package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentUpdateTimeVO {
    private String content;
    private LocalDateTime updateTime;
}
