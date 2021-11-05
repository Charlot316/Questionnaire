package team.buaa.questionnaire.entity.vo;

import lombok.Data;

@Data
public class ChoiceAnswerVO {
    private Integer id; // 选项
    private String content; // 内容
    private Integer count; // 填写人数
    private String percentage; // 百分数
}
