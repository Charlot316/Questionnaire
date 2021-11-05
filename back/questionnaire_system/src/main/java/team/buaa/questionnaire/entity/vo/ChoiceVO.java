package team.buaa.questionnaire.entity.vo;

import lombok.Data;

@Data
public class ChoiceVO {
    private Integer choiceId;
    private String choiceContent;
    private Integer votes;
    private Double percentage;
    private Integer nowCount;
    private Integer allCount;
    private Integer isLimit;
    private Integer isAnswer;
}
