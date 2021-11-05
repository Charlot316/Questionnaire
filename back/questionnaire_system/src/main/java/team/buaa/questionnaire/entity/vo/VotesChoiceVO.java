package team.buaa.questionnaire.entity.vo;

import lombok.Data;

@Data
public class VotesChoiceVO {
    private String choiceContent;
    private Integer votes;
    private Double percentage;
    private Integer ranking;
}
