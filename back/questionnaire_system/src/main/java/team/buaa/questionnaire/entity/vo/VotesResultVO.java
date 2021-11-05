package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VotesResultVO {
    private String name;
    private List<VotesQuestionVO> votesQuestionVOS = new ArrayList<>();
}
