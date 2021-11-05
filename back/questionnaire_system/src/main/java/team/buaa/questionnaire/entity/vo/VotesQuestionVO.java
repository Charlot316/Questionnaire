package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class VotesQuestionVO {
    private String content;
    private List<VotesChoiceVO> votesChoiceVOS = new ArrayList<>();

    public void sort(List<VotesChoiceVO> list) {
        list.sort(new Comparator<VotesChoiceVO>() {
            @Override
            public int compare(VotesChoiceVO o1, VotesChoiceVO o2) {
                if (o1.getVotes() > o2.getVotes()) {
                    return -1;
                } else if (o1.getVotes().equals(o2.getVotes())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }
}
