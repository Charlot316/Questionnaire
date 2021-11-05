package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import team.buaa.questionnaire.entity.domain.Score;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDTO {
    private Integer type; // 0-单选 1-多选 2-下拉 3-单项填空 4-多项填空
    private String content;
    private Integer isCompulsory; // 0-必填 1-选填
    private String introduction;
    private Integer max;
    private Integer isShowVotes;
    private Integer isShowPercentage;
    private Double score;
    private String answer;
    private String analysis;
    private List<ChoiceDTO> choices = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
}
