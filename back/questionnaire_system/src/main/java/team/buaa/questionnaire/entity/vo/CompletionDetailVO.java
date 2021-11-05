package team.buaa.questionnaire.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompletionDetailVO {
    private Integer max; // 空格数
    private List<LocalDateTime> submitTimeList = new ArrayList<>(); // 提交时间
    private List<List<String>> content = new ArrayList<>();
}
