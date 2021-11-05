package team.buaa.questionnaire.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionnaireInfScreenDTO {
    private Integer userId;
    private Integer type; // 0-更新时间倒序 1-更新时间顺序 2-创建时间倒序 3-创建时间顺序 4-问卷量大到小 5-问卷量小到大
    private String keyword; // 关键字
    private Integer status; // 0-编辑 1-保存 2-发布 3-终止 4-已删除
    private Integer isStar; // 0-未收藏 1-收藏
    private Integer current; // 当前页数
    private Integer size; // 页面大小
    private Integer questionnaireType;

    public Integer getCurrent() {
        return current == null ? 0 : current;
    }

    public Integer getSize() {
        return size == null ? 10 : size;
    }
}
