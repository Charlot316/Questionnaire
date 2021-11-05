package team.buaa.questionnaire.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.common.enums.ReturnStatus;
import team.buaa.questionnaire.common.server.WebSocketServer;
import team.buaa.questionnaire.entity.domain.*;
import team.buaa.questionnaire.entity.dto.QuestionSubmitDTO;
import team.buaa.questionnaire.entity.dto.SubmitContentDTO;
import team.buaa.questionnaire.mapper.*;
import team.buaa.questionnaire.service.IQuestionnaireService;
import team.buaa.questionnaire.service.ISubmitContentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubmitContentServiceImpl extends ServiceImpl<SubmitContentMapper, SubmitContent> implements ISubmitContentService {
    @Autowired
    private SubmitRecordMapper submitRecordMapper;
    @Autowired
    private SubmitContentMapper submitContentMapper;
    @Autowired
    private QuestionnaireMapper questionnaireMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private IQuestionnaireService iQuestionnaireService;

    @Override
    public ReturnResponse<String> submitQuestionnaire(SubmitContentDTO submitContentDTO) {
        try {
            Integer userId = submitContentDTO.getUserId();
            Integer questionnaireId = submitContentDTO.getQuestionnaireId();
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            String ip = submitContentDTO.getIp();
            if (questionnaire.getType() == 3) { // 考试问卷提交
                return submitExam(submitContentDTO);
            }
            boolean flag = false;
            if (questionnaire.getStartTime() != null) {
                if (LocalDateTime.now().isBefore(questionnaire.getStartTime())) {
                    return ReturnResponse.packageObject("问卷还未开始", ReturnStatus.FAILURE);
                }
            }
            if (questionnaire.getIsLimit() == 1) {
                if (questionnaire.getNowCount() - 1 < 0) {
                    return ReturnResponse.packageObject("问卷填写数量已满", ReturnStatus.FAILURE);
                } else {
                    questionnaire.setNowCount(questionnaire.getNowCount() - 1);
                    questionnaireMapper.updateById(questionnaire);
                }
            }
            List<QuestionSubmitDTO> questionSubmitDTOS = submitContentDTO.getQuestionSubmitDTOS();
            SubmitRecord submitRecord = new SubmitRecord(questionnaireId, userId, LocalDateTime.now(), ip);
            submitRecordMapper.insert(submitRecord);
            Integer submitRecordId = submitRecord.getId();
            for (QuestionSubmitDTO questionSubmitDTO : questionSubmitDTOS) {
                Integer questionId = questionSubmitDTO.getQuestionId();
                String content = questionSubmitDTO.getContent();
                SubmitContent submitContent = new SubmitContent(submitRecordId, questionId, content);
                Question question = questionMapper.selectById(questionId);
                if (question.getType() == 0 || question.getType() == 1) {
                    Choice choice = choiceMapper.selectOne(new QueryWrapper<Choice>()
                            .eq("question_id", questionId)
                            .eq("choice_id", content));
                    if (choice != null && choice.getIsLimit() == 1) {
                        if (choice.getNowCount() == 0) {
                            return ReturnResponse.packageObject("失败", ReturnStatus.FAILURE);
                        } else {
                            choice.setNowCount(choice.getNowCount() - 1);
                            choiceMapper.updateById(choice);
                            flag = true;
                        }
                    }
                }
                submitContentMapper.insert(submitContent);
            }
            Integer count = questionnaire.getCount() + 1;
            UpdateWrapper<Questionnaire> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", questionnaireId).set("count", count);
            questionnaireMapper.update(null, updateWrapper);
            if (flag) {
                for (Map.Entry<String, WebSocketServer> entry : WebSocketServer.webSocketMap.entrySet()) {
                    if (entry.getKey().startsWith("" + questionnaireId + "&")) {
                        WebSocketServer.webSocketMap.get(entry.getKey()).sendMessage(JSONObject.toJSONString(iQuestionnaireService.getQuestionnaireContent(questionnaire.getUserId(), questionnaireId).getData()));
                    }
                }
            }
            System.out.println("666666666666666666666666");
            return ReturnResponse.OK("提交成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> submitExam(SubmitContentDTO submitContentDTO) {
        try {
            Integer userId = submitContentDTO.getUserId();
            Integer questionnaireId = submitContentDTO.getQuestionnaireId();
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            String ip = submitContentDTO.getIp();
            List<QuestionSubmitDTO> questionSubmitDTOS = submitContentDTO.getQuestionSubmitDTOS();
            SubmitRecord submitRecord = new SubmitRecord(questionnaireId, userId, LocalDateTime.now(), ip);
            submitRecordMapper.insert(submitRecord);
            double score = 0;
            Integer submitRecordId = submitRecord.getId();
            int i;
            for (i = 0; i < questionSubmitDTOS.size(); i++) {
                Integer questionId = questionSubmitDTOS.get(i).getQuestionId();
                String content = questionSubmitDTOS.get(i).getContent();
                SubmitContent submitContent = new SubmitContent(submitRecordId, questionId, content);
                submitContent.setScore(0d);
                Question question = questionMapper.selectById(questionId);
                if (question.getType() == 0) { // 考试单选
                    Choice choice = choiceMapper.selectOne(new QueryWrapper<Choice>()
                            .eq("question_id", questionId)
                            .eq("choice_id", content));
                    if (choice != null && choice.getIsAnswer() == 1) { // 选对了
                        submitContent.setScore(question.getScore());
                        score += question.getScore();
                    }
                    submitContentMapper.insert(submitContent);
                } else if (question.getType() == 3) { // 单项填空
                    if (question.getAnswer() != null && !question.getAnswer().equals("") && question.getAnswer().equals(content)) { // 有答案，需要评分
                        submitContent.setScore(question.getScore());
                        score += question.getScore();
                    }
                    submitContentMapper.insert(submitContent);
                } else if (question.getType() == 1) { // 多选题
                    // 拼出正确选项
                    StringBuilder temp = new StringBuilder();
                    List<Choice> choices = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", questionId));
                    for (int j = 0; j < choices.size(); j++) {
                        if (choices.get(j).getIsAnswer() == 1) {
                            temp.append(j + 1);
                        }
                    }
                    int flag = 0;
                    String correctAnswer = temp.toString();
                    if (correctAnswer.equals("")) { // 不算分
                        flag = 1;
                    }
                    // 拼出填写答案
                    StringBuilder temp1 = new StringBuilder();
                    String realAnswer = null;
                    while (i < questionSubmitDTOS.size()) {
                        if (i + 1 < questionSubmitDTOS.size() && questionId.equals(questionSubmitDTOS.get(i).getQuestionId())
                                && questionId.equals(questionSubmitDTOS.get(i + 1).getQuestionId())) {
                            temp1.append(questionSubmitDTOS.get(i).getContent());
                            submitContentMapper.insert(new SubmitContent(submitRecordId, questionId, questionSubmitDTOS.get(i).getContent()));
                        } else {
                            temp1.append(questionSubmitDTOS.get(i).getContent());
                            realAnswer = temp1.toString();
                            // 算分
                            if (flag == 0 && correctAnswer.equals(realAnswer)) {
                                submitContentMapper.insert(new SubmitContent(submitRecordId, questionId, questionSubmitDTOS.get(i).getContent(), question.getScore()));
                                score += question.getScore();
                            } else {
                                submitContentMapper.insert(new SubmitContent(submitRecordId, questionId, questionSubmitDTOS.get(i).getContent(), 0d));
                            }
                            break;
                        }
                        i++;
                    }
                } else if (question.getType() == 4) { // 多项填空
                    // 拼出正确选项
                    String correctAnswer = null;
                    int flag = 1; // 不算分
                    if (question.getAnswer() != null) { // 算分
                        flag = 0;
                        correctAnswer = question.getAnswer();
                    }
                    // 拼出填写答案
                    StringBuilder temp1 = new StringBuilder();
                    String realAnswer = null;
                    while (i < questionSubmitDTOS.size()) {
                        if (i + 1 < questionSubmitDTOS.size() && questionId.equals(questionSubmitDTOS.get(i).getQuestionId())
                                && questionId.equals(questionSubmitDTOS.get(i + 1).getQuestionId())) {
                            temp1.append(questionSubmitDTOS.get(i).getContent());
                            temp1.append(";");
                            submitContentMapper.insert(new SubmitContent(submitRecordId, questionId, questionSubmitDTOS.get(i).getContent()));
                        } else {
                            temp1.append(questionSubmitDTOS.get(i).getContent());
                            realAnswer = temp1.toString();
                            // 算分
                            if (flag == 0 && correctAnswer.equals(realAnswer)) {
                                submitContentMapper.insert(new SubmitContent(submitRecordId, questionId, questionSubmitDTOS.get(i).getContent(), question.getScore()));
                                score += question.getScore();
                            } else {
                                submitContentMapper.insert(new SubmitContent(submitRecordId, questionId, questionSubmitDTOS.get(i).getContent(), 0d));
                            }
                            break;
                        }
                        i++;
                    }
                }
            }
            SubmitRecord submitRecord1 = new SubmitRecord();
            submitRecord1.setScore(score);
            submitRecordMapper.update(submitRecord1, new UpdateWrapper<SubmitRecord>().eq("id", submitRecordId));
            Integer count = questionnaire.getCount() + 1;
            UpdateWrapper<Questionnaire> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", questionnaireId).set("count", count);
            questionnaireMapper.update(null, updateWrapper);
            return ReturnResponse.OK(submitRecordId + "");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }
}
