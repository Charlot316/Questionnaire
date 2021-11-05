package team.buaa.questionnaire.entity.vo;

import lombok.Data;
import team.buaa.questionnaire.entity.dto.ChoiceDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionVO {
    private Integer questionId;
    private Integer type; // 0-单选 1-多选 2-下拉 3-单项填空 4-多项填空 5-投票单选 6-投票多选
    private String content;
    private Integer isCompulsory; // 0-必填 1-选填
    private String introduction;
    private Integer max;
    private Integer isShowVotes;
    private Integer isShowPercentage;
    private Double score;
    private String answer;
    private List<ChoiceVO> choices = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
}
