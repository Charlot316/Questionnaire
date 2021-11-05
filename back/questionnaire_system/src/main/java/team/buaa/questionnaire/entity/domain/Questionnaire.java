package team.buaa.questionnaire.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Questionnaire {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private String name;
    private String introduction;
    private Integer count; // 已填写数量
    private Integer isStar; // 0-未收藏 1-收藏
    private Integer status; // 0-保存 1-发布  2-暂停 3-已删除
    private LocalDateTime startTime; // 问卷开始时间
    private LocalDateTime endTime; // 问卷结束时间
    private Integer isRepeat; // 0-可重复填写 1-不可重复填写
    private Integer isLogin; // 0-不允许未登录用户填写 1-允许
    private Integer isAppoint; // 0-不给指定用户填写 1-给指定用户填写
    private LocalDateTime releaseTime; // 发布时间
    private String code; // 时间戳码
    private Integer isNumber;
    private Integer type; // 0-普通 1-投票 2-报名 3-考试 4-疫情
    private Integer isLimit; // 0-不限额 1-限额
    private Integer allCount; // 总选项限额
    private Integer nowCount; // 当前份数
    private Integer isOrder; // 0-不乱序 1-乱序
    private Double score; // 考试问卷总分

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Questionnaire(Integer userId, String name, String introduction, Integer count, Integer isStar, Integer status, LocalDateTime startTime, LocalDateTime endTime,
                         Integer isRepeat, Integer isLogin, Integer isAppoint, LocalDateTime releaseTime, String code, Integer isNumber, Integer type) {
        this.userId = userId;
        this.name = name;
        this.introduction = introduction;
        this.count = count;
        this.isStar = isStar;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRepeat = isRepeat;
        this.isLogin = isLogin;
        this.isAppoint = isAppoint;
        this.releaseTime = releaseTime;
        this.code = code;
        this.isNumber = isNumber;
        this.type = type;
    }

    public Questionnaire() {}
}
