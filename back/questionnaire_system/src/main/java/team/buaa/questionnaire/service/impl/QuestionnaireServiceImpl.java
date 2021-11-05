package team.buaa.questionnaire.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import team.buaa.questionnaire.common.config.MD5;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.common.enums.ReturnStatus;
import team.buaa.questionnaire.entity.domain.*;
import team.buaa.questionnaire.entity.dto.*;
import team.buaa.questionnaire.entity.vo.*;
import team.buaa.questionnaire.mapper.*;
import team.buaa.questionnaire.service.IQuestionnaireService;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire> implements IQuestionnaireService {
    @Autowired
    private QuestionnaireMapper questionnaireMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private SubmitContentMapper submitContentMapper;
    @Autowired
    private SubmitRecordMapper submitRecordMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExamMapper examMapper;

    @Override
    public ReturnResponse<Page<QuestionnaireInfVO>> getQuestionnaireInf(QuestionnaireInfScreenDTO questionnaireInfScreenDTO) {
        Integer userId = questionnaireInfScreenDTO.getUserId();
        Integer type = questionnaireInfScreenDTO.getType() == null ? (Integer) 0 : questionnaireInfScreenDTO.getType();
        String keyword = questionnaireInfScreenDTO.getKeyword();
        Integer status = questionnaireInfScreenDTO.getStatus();
        Integer isStar = questionnaireInfScreenDTO.getIsStar();
        Integer current = questionnaireInfScreenDTO.getCurrent();
        Integer size = questionnaireInfScreenDTO.getSize();
        Integer questionnaireType = questionnaireInfScreenDTO.getQuestionnaireType();
        try {
            QueryWrapper<Questionnaire> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.ne("status", 3);
            if (keyword != null) {
                queryWrapper.like("name", keyword);
            }
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            if (isStar != null) {
                queryWrapper.eq("is_star", isStar);
            }
            if (questionnaireType != null) {
                queryWrapper.eq("type", questionnaireType);
            }
            switch (type) {
                case 0:
                    queryWrapper.orderByDesc("create_time");
                    break;
                case 1:
                    queryWrapper.orderByAsc("create_time");
                    break;
                case 2:
                    queryWrapper.orderByDesc("update_time");
                    break;
                case 3:
                    queryWrapper.orderByAsc("update_time");
                    break;
                case 4:
                    queryWrapper.orderByDesc("count");
                    break;
                case 5:
                    queryWrapper.orderByAsc("count");
                    break;
            }
            Page<QuestionnaireInfVO> page = new Page<>();
            page.setCurrent(current);
            page.setSize(size);
            Page<QuestionnaireInfVO> questionnaireInfVOS = questionnaireMapper.getQuestionnaireInf(page, queryWrapper);
            return ReturnResponse.OK(questionnaireInfVOS);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> createQuestionnaire(QuestionnaireDTO questionnaireDTO) {
        Integer userId = questionnaireDTO.getUserId();
        String name = questionnaireDTO.getName();
        String introduction = questionnaireDTO.getIntroduction();
        Integer count = 0;
        Integer isStar = 0;
        Integer status = 0;
        LocalDateTime startTime = questionnaireDTO.getStartTime();
        LocalDateTime endTime = questionnaireDTO.getEndTime();
        Integer isRepeat = questionnaireDTO.getIsRepeat();
        Integer isLogin = questionnaireDTO.getIsLogin();
        Integer isAppoint = questionnaireDTO.getIsAppoint();
        Integer isNumber = questionnaireDTO.getIsNumber();
        Integer type = questionnaireDTO.getType(); // 0-普通 1-投票 2-报名 3-考试 4-疫情
        Integer isLimit;
        Integer allCount = questionnaireDTO.getAllCount();
        Integer nowCount = questionnaireDTO.getAllCount();
        Integer isOrder;
        if (questionnaireDTO.getIsLimit() == null) {
            isLimit = 0;
        } else {
            isLimit = questionnaireDTO.getIsLimit();
        }
        if (questionnaireDTO.getIsOrder() == null) {
            isOrder = 0;
        } else {
            isOrder = questionnaireDTO.getIsOrder();
        }
        List<QuestionDTO> questionDTOS = questionnaireDTO.getQuestions();
        try {
            if (endTime == null) {
                endTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
            }
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setUserId(userId);
            questionnaire.setName(name);
            questionnaire.setIntroduction(introduction);
            questionnaire.setCount(count);
            questionnaire.setIsStar(isStar);
            questionnaire.setStatus(status);
            questionnaire.setStartTime(startTime);
            questionnaire.setEndTime(endTime);
            questionnaire.setIsRepeat(isRepeat);
            questionnaire.setIsLogin(isLogin);
            questionnaire.setIsAppoint(isAppoint);
            questionnaire.setReleaseTime(null);
            questionnaire.setCode(null);
            questionnaire.setIsNumber(isNumber);
            questionnaire.setType(type);
            questionnaire.setIsLimit(isLimit);
            if (isLimit == 1) {
                questionnaire.setAllCount(allCount);
                questionnaire.setNowCount(nowCount);
            }
            questionnaire.setIsOrder(isOrder);
            questionnaireMapper.insert(questionnaire);
            Integer questionnaireId = questionnaire.getId();
            for (QuestionDTO questionDTO : questionDTOS) {
                Question question = new Question();
                question.setQuestionnaireId(questionnaireId);
                question.setType(questionDTO.getType());
                question.setContent(questionDTO.getContent());
                question.setIsCompulsory(questionDTO.getIsCompulsory());
                question.setIntroduction(questionDTO.getIntroduction());
                if (questionDTO.getType() == 2 || questionDTO.getType() == 4) { // 多项填空将空格数存在max_score
                    question.setMaxScore(questionDTO.getMax());
                }
                // 5-投票单选 6-投票多选
                if (questionDTO.getType() == 5 || questionDTO.getType() == 6) {
                    question.setIsShowVotes(questionDTO.getIsShowVotes());
                    question.setIsShowPercentage(questionDTO.getIsShowPercentage());
                }
                if (type == 3) {
                    if (questionDTO.getScore() != null) {
                        question.setScore(questionDTO.getScore());
                    }
                    if (question.getType() == 3) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            question.setAnswer(questionDTO.getAnswer());
                        }
                    }
                    if (question.getType() == 4) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            String[] split = questionDTO.getAnswer().split(";");
                            if (split.length != 0) {
                                question.setAnswer(questionDTO.getAnswer());
                            }
                        }
                    }
                    if (questionDTO.getAnalysis() != null) {
                        question.setAnalysis(questionDTO.getAnalysis());
                    }
                }
                questionMapper.insert(question);
                Integer questionId = question.getId();
                if (questionDTO.getType() == 0 || questionDTO.getType() == 1 || questionDTO.getType() == 5 ||
                        questionDTO.getType() == 6) {
                    List<ChoiceDTO> choiceDTOS = questionDTO.getChoices();
                    for (ChoiceDTO choiceDTO : choiceDTOS) {
                        Choice choice = new Choice();
                        choice.setQuestionId(questionId);
                        choice.setChoiceId(choiceDTO.getChoiceId());
                        choice.setChoiceContent(choiceDTO.getChoiceContent());
                        if (choiceDTO.getIsLimit() == null) {
                            choice.setIsLimit(0);
                        } else {
                            choice.setIsLimit(choiceDTO.getIsLimit());
                        }
                        if (choice.getIsLimit() == 1) {
                            choice.setAllCount(choiceDTO.getAllCount());
                            choice.setNowCount(choiceDTO.getAllCount());
                        }
                        if (type == 3) {
                            if (choiceDTO.getIsAnswer() == null) {
                                choice.setIsAnswer(0);
                            } else {
                                choice.setIsAnswer(choiceDTO.getIsAnswer());
                            }
                        }
                        choiceMapper.insert(choice);
                    }
                } else if (questionDTO.getType() == 2) {
                    List<String> texts = questionDTO.getTexts();
                    Integer i = 1;
                    for (String scoreContent : texts) {
                        Score score = new Score();
                        score.setScoreId(i);
                        score.setQuestionId(questionId);
                        score.setScoreContent(scoreContent);
                        scoreMapper.insert(score);
                        i += 1;
                    }
                }
            }
            return ReturnResponse.OK("" + questionnaireId);

        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> copyQuestionnaire(ReplicationDTO replicationDTO) {
        try {
            Integer questionnaireId = replicationDTO.getQuestionnaireId();
            String name = replicationDTO.getName();
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            System.out.println(questionnaire);
            questionnaire.setName(name);
            questionnaire.setCount(0);
            questionnaire.setStatus(0);
            questionnaireMapper.insert(questionnaire);
            List<Question> questions = questionMapper.selectList(new QueryWrapper<Question>().eq("questionnaire_id", questionnaireId));
            questionnaireId = questionnaire.getId();
            for (Question question : questions) {
                Integer questionId = question.getId();
                Integer type = question.getType();
                question.setQuestionnaireId(questionnaireId);
                questionMapper.insert(question);
                if (type == 0 || type == 1 || type == 5 || type == 6) {
                    List<Choice> choices = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", questionId));
                    questionId = question.getId();
                    for (Choice choice : choices) {
                        choice.setQuestionId(questionId);
                        choiceMapper.insert(choice);
                    }
                } else if (type == 2) {
                    List<Score> scores = scoreMapper.selectList(new QueryWrapper<Score>().eq("question_id", questionId));
                    questionId = question.getId();
                    for (Score score : scores) {
                        score.setQuestionId(questionId);
                        scoreMapper.insert(score);
                    }
                }
            }
            return ReturnResponse.OK("复制问卷成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> starQuestionnaire(StarDTO starDTO) {
        try {
            UpdateWrapper<Questionnaire> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", starDTO.getQuestionnaireId()).set("is_star", 1);
            questionnaireMapper.update(null, wrapper);
            return ReturnResponse.OK("收藏问卷成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> noStarQuestionnaire(StarDTO starDTO) {
        try {
            UpdateWrapper<Questionnaire> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", starDTO.getQuestionnaireId()).set("is_star", 0);
            questionnaireMapper.update(null, wrapper);
            return ReturnResponse.OK("取消收藏问卷成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse publishQuestionnaire(StarDTO starDTO) {
        try {
            UpdateWrapper<Questionnaire> wrapper = new UpdateWrapper<>();
            wrapper
                    .eq("id", starDTO.getQuestionnaireId());
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setStatus(1);
            questionnaire.setReleaseTime(LocalDateTime.now());
            questionnaire.setUpdateTime(LocalDateTime.now());
            String code = MD5.getInstance().getMD5("" + LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
            questionnaire.setCode(code);
            questionnaireMapper.update(questionnaire, wrapper);
            return ReturnResponse.OK(code);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> stopQuestionnaire(StarDTO starDTO) {
        try {
            UpdateWrapper<Questionnaire> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", starDTO.getQuestionnaireId());
            Questionnaire questionnaire = new Questionnaire();
            String code = MD5.getInstance().getMD5("" + LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
            questionnaire.setCode(code);
            questionnaire.setStatus(2);
            questionnaireMapper.update(questionnaire, wrapper);
            return ReturnResponse.OK("暂停问卷成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> deleteQuestionnaire(StarDTO starDTO) {
        try {
            UpdateWrapper<Questionnaire> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", starDTO.getQuestionnaireId());
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setStatus(3);
            questionnaireMapper.update(questionnaire, wrapper);
            return ReturnResponse.OK("删除问卷成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse getQuestionnaireContent(Integer userId, Integer questionnaireId) {
        try {
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            if (!userId.equals(questionnaire.getUserId())) { // 该问卷不是当前登录用户创建的
                return ReturnResponse.packageObject("无访问权限", ReturnStatus.FAILURE);
            }
            QuestionnaireVO questionnaireVO = new QuestionnaireVO();
            questionnaireVO.setName(questionnaire.getName());
            questionnaireVO.setIntroduction(questionnaire.getIntroduction());
            questionnaireVO.setStartTime(questionnaire.getStartTime());
            questionnaireVO.setEndTime(questionnaire.getEndTime());
            questionnaireVO.setIsRepeat(questionnaire.getIsRepeat());
            questionnaireVO.setIsLogin(questionnaire.getIsLogin());
            questionnaireVO.setIsAppoint(questionnaire.getIsAppoint());
            questionnaireVO.setIsNumber(questionnaire.getIsNumber());
            questionnaireVO.setType(questionnaire.getType());
            questionnaireVO.setIsLimit(questionnaire.getIsLimit());
            if (questionnaire.getIsLimit() == 1) {
                questionnaireVO.setAllCount(questionnaire.getAllCount());
            } else {
                questionnaireVO.setAllCount(100);
            }
            questionnaireVO.setIsOrder(questionnaire.getIsOrder());
            List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("questionnaire_id", questionnaireId));
            for (Question question : questionList) {
                QuestionVO questionVO = new QuestionVO();
                questionVO.setQuestionId(question.getId());
                questionVO.setType(question.getType());
                questionVO.setContent(question.getContent());
                questionVO.setIsCompulsory(question.getIsCompulsory());
                questionVO.setIntroduction(question.getIntroduction());
                if (questionnaire.getType() == 1) {
                    questionVO.setIsShowVotes(question.getIsShowVotes());
                    questionVO.setIsShowPercentage(question.getIsShowPercentage());
                }
                if (question.getType() == 2 || question.getType() == 4) { // 多项填空将空格数存在max_score
                    questionVO.setMax(question.getMaxScore());
                }
                if (questionnaire.getType() == 3) {
                    questionVO.setScore(question.getScore()); // 考试题目分值
                    questionVO.setAnswer(question.getAnswer());
                }
                List<Choice> choiceList = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", question.getId()));
                for (Choice choice : choiceList) {
                    ChoiceVO choiceVO = new ChoiceVO();
                    choiceVO.setChoiceId(choice.getChoiceId());
                    choiceVO.setChoiceContent(choice.getChoiceContent());
                    if (choice.getIsLimit() == 1) {
                        choiceVO.setIsLimit(1);
                        choiceVO.setAllCount(choice.getAllCount());
                        choiceVO.setNowCount(choice.getNowCount());
                    } else {
                        choiceVO.setIsLimit(0);
                        choiceVO.setAllCount(10);
                    }
                    if (questionnaire.getType() == 1) {
                        if (question.getIsShowVotes() == 1) {
                            QueryWrapper<SubmitContent> queryWrapper = new QueryWrapper<SubmitContent>().
                                    eq("question_id", question.getId()).
                                    eq("content", "" + choice.getChoiceId());
                            choiceVO.setVotes(submitContentMapper.selectCount(queryWrapper));
                        }
                        if (question.getIsShowPercentage() == 1) {
                            QueryWrapper<SubmitContent> queryWrapper1 = new QueryWrapper<SubmitContent>()
                                    .eq("question_id", question.getId())
                                    .eq("content", "" + choice.getChoiceId());
                            QueryWrapper<SubmitRecord> questionnaire2 = new QueryWrapper<SubmitRecord>()
                                    .eq("questionnaire_id", questionnaireId);
                            int votes = submitContentMapper.selectCount(queryWrapper1);
                            int counts = submitRecordMapper.selectCount(questionnaire2);
                            if (counts == 0) {
                                choiceVO.setPercentage(0.0);
                            } else {
                                choiceVO.setPercentage(Double.parseDouble(String.format("%.1f", (double) votes / counts * 100.0)));
                            }
                        }
                    }
                    if (questionnaire.getType() == 3) {
                        choiceVO.setIsAnswer(choice.getIsAnswer());
                    }
                    questionVO.getChoices().add(choiceVO);
                }
                questionVO.setTexts(scoreMapper.getScoreContents(new QueryWrapper<Score>().eq("question_id", question.getId())));
                questionnaireVO.getQuestions().add(questionVO);
            }
            return ReturnResponse.OK(questionnaireVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse getQuestionnaireContentByCode(CheckDTO checkDTO) {
        try {
            Integer questionnaireId = checkDTO.getQuestionnaireId();
            String code = checkDTO.getCode();
            String realCode = questionnaireMapper.selectById(questionnaireId).getCode();
            // 0-未登录 1-已登录
            Integer isLogin = checkDTO.getIsLogin();
            // 0-不允许未登录用户填写 1-允许未登录用户填写
            Integer isLogin1 = questionnaireMapper.selectById(questionnaireId).getIsLogin();
            // 0-不可重复填写 1-可重复填写
            Integer isRepeat = questionnaireMapper.selectById(questionnaireId).getIsRepeat();
            Integer status = questionnaireMapper.selectById(questionnaireId).getStatus();
            if (status != 1) { // 问卷未发布
                return ReturnResponse.packageObject("该问卷已失效", ReturnStatus.FAILURE);
            }
            if (!code.equals(realCode)) {
                return ReturnResponse.packageObject("该问卷已失效", ReturnStatus.FAILURE);
            }
            // 考试问卷
            if (questionnaireMapper.selectById(questionnaireId).getType() == 3) {
                CheckExamDTO checkExamDTO = new CheckExamDTO();
                checkExamDTO.setIp(checkDTO.getIp());
                checkExamDTO.setQuestionnaireId(questionnaireId);
                return getExamByCode(checkExamDTO);
            }
            if (LocalDateTime.now().isAfter(questionnaireMapper.selectById(questionnaireId).getEndTime())) {
                return ReturnResponse.packageObject("该问卷已过期", ReturnStatus.FAILURE);
            }
            if (isLogin == 0 && isLogin1 == 0) {
                return ReturnResponse.packageObject("请先登录再填写问卷", ReturnStatus.FAILURE);
            }
            if (isRepeat == 0) { // 判断ip是否重复
                String ip = checkDTO.getIp();
                List<SubmitRecord> submitRecords = submitRecordMapper.selectList(new QueryWrapper<SubmitRecord>()
                        .eq("questionnaire_id", questionnaireId)
                        .eq("ip", ip));
                if (!submitRecords.isEmpty()) {
                    return ReturnResponse.packageObject("不可重复填写问卷", ReturnStatus.FAILURE);
//                } else {
//                    return getQuestionnaireContent(questionnaireId);
                }
            }
            // 返回问卷内容
            Integer userId = questionnaireMapper.selectById(questionnaireId).getUserId();
            return getQuestionnaireContent(userId, questionnaireId);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse getQuestionnaireAnswering(Integer questionnaireId, Integer userId1) {
        try {
            if (!userId1.equals(questionnaireMapper.selectById(questionnaireId).getUserId())) { // 该问卷不是当前登录用户创建的
                return ReturnResponse.packageObject("无访问权限", ReturnStatus.FAILURE);
            }
            AnsweringVO answeringVO = new AnsweringVO();
            List<SubmitRecord> submitRecords = submitRecordMapper.selectList(new QueryWrapper<SubmitRecord>().eq("questionnaire_id", questionnaireId));
            List<String> questionContents = questionMapper.getQuestionContentByQuestionnaireId(new QueryWrapper<Question>().eq("questionnaire_id", questionnaireId));
            Integer isNumber = questionnaireMapper.selectById(questionnaireId).getIsNumber();
            for (int i = 0; i < questionContents.size(); i++) {
                QuestionContentVO questionContentVO = new QuestionContentVO();
                questionContentVO.setId(i + 1);
                if (isNumber == 0) {
                    int j = i + 1;
                    questionContentVO.setContent(j + "." + questionContents.get(i));
                } else {
                    questionContentVO.setContent(questionContents.get(i));
                }
                answeringVO.getQuestionContentVOS().add(questionContentVO);
            }
            for (SubmitRecord submitRecord : submitRecords) {
                Integer userId = submitRecord.getUserId();
                Integer submitRecordId = submitRecord.getId();
                UserAnsweringVO userAnsweringVO = new UserAnsweringVO();
                userAnsweringVO.setUsername(userMapper.selectById(userId).getUsername());
                userAnsweringVO.setTime(submitRecord.getCreateTime());
                List<QuestionAnsweringVO> questionAnsweringVOS = submitContentMapper.getAnswerQuestionContentByRecordId(new QueryWrapper<SubmitContent>().eq("submit_record_id", submitRecordId));
                List<String> answerContent = new ArrayList<>();
                Integer nextId = 0;
                String content = "";
                char ch;
                Integer flag = 0;
                int i = 1;
                AnswerContentVO answerContentVO;
                int nowId = questionAnsweringVOS.get(0).getQuestionId();
                for (QuestionAnsweringVO questionAnsweringVO : questionAnsweringVOS) {
                    if (nowId != questionAnsweringVO.getQuestionId()) {
                        nowId = questionAnsweringVO.getQuestionId();
                        answerContentVO = new AnswerContentVO();
                        answerContentVO.setId(i);
                        answerContentVO.setAnswerContent(content);
                        userAnsweringVO.getAnswerContentVOS().add(answerContentVO);
                        i += 1;
                        content = "";
                    }
                    switch (questionAnsweringVO.getType()) {
                        case 5:
                        case 0:
                            if (questionAnsweringVO.getContent().equals("")) {
                                content = " ";
                            } else {
                                ch = (char) (Integer.parseInt(questionAnsweringVO.getContent()) - 1 + 'A');
                                content = "" + ch;
                            }
                            break;
                        case 6:
                        case 1:
                            if (!questionAnsweringVO.getContent().equals("")) {
                                ch = (char) (Integer.parseInt(questionAnsweringVO.getContent()) - 1 + 'A');
                                content = content + ch;
                            } else {
                                content = " ";
                            }
                            break;
                        case 2:
                            if (questionAnsweringVO.getContent().equals("")) {
                                content = " ";
                            } else {
                                content = questionAnsweringVO.getContent() + "星";
                            }
                            break;
                        case 7:
                        case 3:
                            if (questionAnsweringVO.getContent().equals("")) {
                                content = " ";
                            } else {
                                content = questionAnsweringVO.getContent();
                            }
                            break;
                        case 4:
                            if (!questionAnsweringVO.getContent().equals("")) {
                                content = content + questionAnsweringVO.getContent() + "┋";
                            } else {
                                content = content + " " + "┋";
                            }
                            break;
                    }
                }
                if (!content.equals("")) {
                    answerContentVO = new AnswerContentVO();
                    answerContentVO.setId(i);
                    answerContentVO.setAnswerContent(content);
                    userAnsweringVO.getAnswerContentVOS().add(answerContentVO);
                }
//                userAnsweringVO.setAnswerContent(submitContentMapper.getAnsweringContentByRecordId(new QueryWrapper<SubmitContent>().eq("submit_record_id", submitRecordId)));
                answeringVO.getUserAnsweringVOS().add(userAnsweringVO);
            }
            return ReturnResponse.OK(answeringVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> getAnsweringToExcel(HttpServletResponse response, ExcelExportDTO excelExportDTO) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String xlsFile_name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
            Integer questionnaireId = excelExportDTO.getQuestionnaireId();
            String name = excelExportDTO.getName();
            Integer userId = questionnaireMapper.selectById(questionnaireId).getUserId();
            AnsweringVO answeringVO = (AnsweringVO) getQuestionnaireAnswering(questionnaireId, userId).getData();
            List<QuestionContentVO> questionContentVOS = answeringVO.getQuestionContentVOS();
            List<String> questionContents = new ArrayList<>();
            for (QuestionContentVO questionContentVO : questionContentVOS) {
                questionContents.add(questionContentVO.getContent());
            }

            List<UserAnsweringVO> userAnsweringVOS = answeringVO.getUserAnsweringVOS();
            int rowCount = userAnsweringVOS.size();
            int questionCount = questionContents.size();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(questionnaireMapper.selectById(questionnaireId).getName());
            XSSFRow row;
            XSSFCell cell;
            for (int i = 0; i <= rowCount; i++) {
                row = sheet.createRow(i);
                if (i == 0) {
                    for (int j = 0; j <= questionCount + 1; j++) {
                        cell = row.createCell(j);
                        if (j == 0) {
                            cell.setCellValue("用户名");
                        } else if (j == 1) {
                            cell.setCellValue("填写时间");
                        } else{
                            cell.setCellValue(questionContents.get(j - 2));
                        }
                    }
                } else {
                    cell = row.createCell(0);
                    cell.setCellValue(userAnsweringVOS.get(i - 1).getUsername());
                    cell = row.createCell(1);
                    ZoneId zoneId = ZoneId.systemDefault();
                    ZonedDateTime zonedDateTime = userAnsweringVOS.get(i - 1).getTime().atZone(zoneId);
                    cell.setCellValue(dateFormat.format(Date.from(zonedDateTime.toInstant())));
                    for (int j = 2; j <= questionCount + 1; j++) {
                        cell = row.createCell(j);
                        if (userAnsweringVOS.get(i - 1).getAnswerContentVOS().size() < j - 1) {
                            cell.setCellValue(" ");
                        } else {
                            cell.setCellValue(userAnsweringVOS.get(i - 1).getAnswerContentVOS().get(j - 2).getAnswerContent());
                        }
                    }
                }
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + xlsFile_name);
            response.flushBuffer();
            OutputStream outputStream = response.getOutputStream();
            workbook.write(response.getOutputStream());
            workbook.close();
            outputStream.flush();
            outputStream.close();
            return null;
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> editQuestionnaireSave(QuestionnaireEditDTO questionnaireEditDTO) {
        Integer id = questionnaireEditDTO.getId();
        Integer userId = questionnaireEditDTO.getUserId();
        String name = questionnaireEditDTO.getName();
        String introduction = questionnaireEditDTO.getIntroduction();
        Integer count = questionnaireEditDTO.getCount();
        Integer isStar = questionnaireEditDTO.getIsStar();
        Integer status = 0; // 修改完状态为 0-保存
        LocalDateTime startTime = questionnaireEditDTO.getStartTime();
        LocalDateTime endTime = questionnaireEditDTO.getEndTime();
        Integer isRepeat = questionnaireEditDTO.getIsRepeat();
        Integer isLogin = questionnaireEditDTO.getIsLogin();
        Integer isAppoint = questionnaireEditDTO.getIsAppoint();
        Integer isNumber = questionnaireEditDTO.getIsNumber();
        Integer type = questionnaireEditDTO.getType();
        Integer isLimit;
        Integer allCount = questionnaireEditDTO.getAllCount();
        Integer isOrder;
        if (questionnaireEditDTO.getIsLimit() == null) {
            isLimit = 0;
        } else {
            isLimit = questionnaireEditDTO.getIsLimit();
        }
        if (questionnaireEditDTO.getIsOrder() == null) {
            isOrder = 0;
        } else {
            isOrder = questionnaireEditDTO.getIsOrder();
        }
        List<QuestionDTO> questionDTOS = questionnaireEditDTO.getQuestions();
        try {
            if (endTime == null) {
                endTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
            }
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setUserId(userId);
            questionnaire.setName(name);
            questionnaire.setIntroduction(introduction);
            questionnaire.setCount(count);
            questionnaire.setIsStar(isStar);
            questionnaire.setStatus(status);
            questionnaire.setStartTime(startTime);
            questionnaire.setEndTime(endTime);
            questionnaire.setIsRepeat(isRepeat);
            questionnaire.setIsLogin(isLogin);
            questionnaire.setIsAppoint(isAppoint);
            questionnaire.setReleaseTime(null);
            questionnaire.setCode(null);
            questionnaire.setIsNumber(isNumber);
            questionnaire.setType(type);
            questionnaire.setIsLimit(isLimit);
            if (isLimit == 1) {
                Integer oldAllCount = questionnaireMapper.selectById(id).getAllCount();
                Integer oldNowCount = questionnaireMapper.selectById(id).getNowCount();
                questionnaire.setAllCount(allCount);
                if (oldAllCount == null) {
                    oldAllCount = 0;
                    oldNowCount = 0;
                }
                questionnaire.setNowCount(oldNowCount + allCount - oldAllCount);
            }
            questionnaire.setIsOrder(isOrder);
            UpdateWrapper<Questionnaire> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            questionnaireMapper.update(questionnaire, wrapper);
            List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("questionnaire_id", id));
            int i;
            for (i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);
                QuestionDTO questionDTO = questionDTOS.get(i);
                Question question1 = new Question();
                // type不能改变
                question1.setContent(questionDTO.getContent());
                question1.setIsCompulsory(questionDTO.getIsCompulsory());
                question1.setIntroduction(questionDTO.getIntroduction());
                if (question.getType() == 2 || question.getType() == 4) { // 多项填空将空格数存在max_score
                    question1.setMaxScore(questionDTO.getMax());
                }
                if (question.getType() == 5 || question.getType() == 6) {
                    question1.setIsShowVotes(questionDTO.getIsShowVotes());
                    question1.setIsShowPercentage(questionDTO.getIsShowPercentage());
                }
                if (type == 3) {
                    if (questionDTO.getScore() != null) {
                        question1.setScore(questionDTO.getScore());
                    }
                    if (question.getType() == 3) {
//                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            question1.setAnswer(questionDTO.getAnswer());
//                        }
                    }
                    if (question.getType() == 4) {
//                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
//                            String[] split = questionDTO.getAnswer().split(";");
//                            if (split.length != 0) {
                                question1.setAnswer(questionDTO.getAnswer());
//                            }
//                        }
                    }
//                    if (questionDTO.getAnalysis() != null) {
                        question1.setAnalysis(questionDTO.getAnalysis());
//                    }
                }
                questionMapper.update(question1, new UpdateWrapper<Question>().eq("id", question.getId()));
                if (question.getType() == 0 || question.getType() == 1 || questionDTO.getType() == 5 ||
                        questionDTO.getType() == 6) { // 选择题
                    List<Choice> choiceList = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", question.getId()));
                    List<ChoiceDTO> choiceDTOS = questionDTO.getChoices();
                    int j;
                    for (j = 0; j < choiceList.size(); j++) {
                        Choice choice = choiceList.get(j);
                        ChoiceDTO choiceDTO = choiceDTOS.get(j);
                        Choice choice1 = new Choice();
                        choice1.setChoiceContent(choiceDTO.getChoiceContent());
                        if (choiceDTO.getIsLimit() == null) {
                            choice1.setIsLimit(0);
                        } else {
                            choice1.setIsLimit(choiceDTO.getIsLimit());
                        }
                        if (choice.getIsLimit() == 1) {
                            Integer oldAllCount1 = choice.getAllCount();
                            Integer oldNowCount1 = choice.getNowCount();
                            choice1.setAllCount(choiceDTO.getAllCount());
                            // 如果nowCount < 0则置0
                            if (oldAllCount1 == null) {
                                oldAllCount1 = 0;
                                oldNowCount1 = 0;
                            }
                            choice1.setNowCount(Math.max(oldNowCount1 + choiceDTO.getAllCount() - oldAllCount1, 0));
                        }
                        if (type == 3) {
                            if (choiceDTO.getIsAnswer() == null) {
                                choice1.setIsAnswer(0);
                            } else {
                                choice1.setIsAnswer(choiceDTO.getIsAnswer());
                            }
                        }
                        choiceMapper.update(choice1, new UpdateWrapper<Choice>()
                                .eq("choice_id", choice.getChoiceId()).eq("question_id", question.getId()));
                    }
                    // 增加选项
                    for (; j < choiceDTOS.size(); j++) {
                        ChoiceDTO choiceDTO = choiceDTOS.get(j);
                        Choice choice = new Choice();
                        choice.setQuestionId(question.getId());
                        choice.setChoiceId(choiceDTO.getChoiceId());
                        choice.setChoiceContent(choiceDTO.getChoiceContent());
                        if (choiceDTO.getIsLimit() == null) {
                            choice.setIsLimit(0);
                        } else {
                            choice.setIsLimit(choiceDTO.getIsLimit());
                        }
                        if (choice.getIsLimit() == 1) {
                            choice.setAllCount(choiceDTO.getAllCount());
                            choice.setNowCount(choiceDTO.getAllCount());
                        }
                        if (type == 3) {
                            if (choiceDTO.getIsAnswer() == null) {
                                choice.setIsAnswer(0);
                            } else {
                                choice.setIsAnswer(choiceDTO.getIsAnswer());
                            }
                        }
                        choiceMapper.insert(choice);
                    }
                } else if (question.getType() == 2) { // 得分题
                    List<Score> scoreList = scoreMapper.selectList(new QueryWrapper<Score>().eq("question_id", question.getId()));
                    List<String> texts = questionDTO.getTexts();
                    int j;
                    for (j = 0; j < scoreList.size(); j++) {
                        Score score = scoreList.get(j);
                        Score score1 = new Score();
                        score1.setScoreContent(texts.get(j));
                        scoreMapper.update(score1, new UpdateWrapper<Score>()
                                .eq("score_id", score.getScoreId()).eq("question_id", question.getId()));
                    }
                    // 增加选项
                    for (; j < texts.size(); j++) {
                        Score score = new Score();
                        score.setQuestionId(question.getId());
                        score.setScoreId(j + 1);
                        score.setScoreContent(texts.get(j));
                        scoreMapper.insert(score);
                    }
                }
            }
            // 增加题目
            for (; i < questionDTOS.size(); i++) {
                QuestionDTO questionDTO = questionDTOS.get(i);
                Question question = new Question();
                question.setQuestionnaireId(id);
                question.setType(questionDTO.getType());
                question.setContent(questionDTO.getContent());
                question.setIsCompulsory(questionDTO.getIsCompulsory());
                question.setIntroduction(questionDTO.getIntroduction());
                if (questionDTO.getType() == 2 || questionDTO.getType() == 4) { // 多项填空将空格数存在max_score
                    question.setMaxScore(questionDTO.getMax());
                }
                if (questionDTO.getType() == 5 || questionDTO.getType() == 6) {
                    question.setIsShowVotes(questionDTO.getIsShowVotes());
                    question.setIsShowPercentage(questionDTO.getIsShowPercentage());
                }
                if (type == 3) {
                    if (questionDTO.getScore() != null) {
                        question.setScore(questionDTO.getScore());
                    }
                    if (question.getType() == 3) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            question.setAnswer(questionDTO.getAnswer());
                        }
                    }
                    if (question.getType() == 4) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            String[] split = questionDTO.getAnswer().split(";");
                            if (split.length != 0) {
                                question.setAnswer(questionDTO.getAnswer());
                            }
                        }
                    }
                    if (questionDTO.getAnalysis() != null) {
                        question.setAnalysis(questionDTO.getAnalysis());
                    }
                }
                questionMapper.insert(question);
                Integer questionId = question.getId();
                if (questionDTO.getType() == 0 || questionDTO.getType() == 1 || questionDTO.getType() == 5 ||
                        questionDTO.getType() == 6) {
                    List<ChoiceDTO> choiceDTOS = questionDTO.getChoices();
                    for (ChoiceDTO choiceDTO : choiceDTOS) {
                        Choice choice = new Choice();
                        choice.setQuestionId(questionId);
                        choice.setChoiceId(choiceDTO.getChoiceId());
                        choice.setChoiceContent(choiceDTO.getChoiceContent());
                        if (choiceDTO.getIsLimit() == null) {
                            choice.setIsLimit(0);
                        } else {
                            choice.setIsLimit(choiceDTO.getIsLimit());
                        }
                        if (choice.getIsLimit() == 1) {
                            choice.setAllCount(choiceDTO.getAllCount());
                            choice.setNowCount(choiceDTO.getAllCount());
                        }
                        if (type == 3) {
                            if (choiceDTO.getIsAnswer() == null) {
                                choice.setIsAnswer(0);
                            } else {
                                choice.setIsAnswer(choiceDTO.getIsAnswer());
                            }
                        }
                        choiceMapper.insert(choice);
                    }
                } else if (questionDTO.getType() == 2) {
                    List<String> texts = questionDTO.getTexts();
                    Integer idx = 1;
                    for (String scoreContent : texts) {
                        Score score = new Score();
                        score.setScoreId(idx);
                        score.setQuestionId(questionId);
                        score.setScoreContent(scoreContent);
                        scoreMapper.insert(score);
                        idx += 1;
                    }
                }
            }
            return ReturnResponse.OK(id + "");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> editQuestionnaireDelete(QuestionnaireEditDeleteDTO questionnaireEditDeleteDTO) {
        Integer id = questionnaireEditDeleteDTO.getId();
        Integer userId = questionnaireEditDeleteDTO.getUserId();
        // 先将问卷信息update
        String name = questionnaireEditDeleteDTO.getName();
        String introduction = questionnaireEditDeleteDTO.getIntroduction();
        Integer count = 0; // 删除所有答卷
        Integer isStar = questionnaireEditDeleteDTO.getIsStar();
        Integer status = 0; // 修改完状态为 0-保存
        LocalDateTime startTime = questionnaireEditDeleteDTO.getStartTime();
        LocalDateTime endTime = questionnaireEditDeleteDTO.getEndTime();
        Integer isRepeat = questionnaireEditDeleteDTO.getIsRepeat();
        Integer isLogin = questionnaireEditDeleteDTO.getIsLogin();
        Integer isAppoint = questionnaireEditDeleteDTO.getIsAppoint();
        Integer isNumber = questionnaireEditDeleteDTO.getIsNumber();
        Integer type1 = questionnaireEditDeleteDTO.getType();
        Integer isLimit;
        Integer allCount = questionnaireEditDeleteDTO.getAllCount();
        Integer nowCount = questionnaireEditDeleteDTO.getAllCount();
        Integer isOrder;
        if (questionnaireEditDeleteDTO.getIsLimit() == null) {
            isLimit = 0;
        } else {
            isLimit = questionnaireEditDeleteDTO.getIsLimit();
        }
        if (questionnaireEditDeleteDTO.getIsOrder() == null) {
            isOrder = 0;
        } else {
            isOrder = questionnaireEditDeleteDTO.getIsOrder();
        }
        List<QuestionDTO> questionDTOS = questionnaireEditDeleteDTO.getQuestions();
        try {
            if (endTime == null) {
                endTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
            }
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setUserId(userId);
            questionnaire.setName(name);
            questionnaire.setIntroduction(introduction);
            questionnaire.setCount(count);
            questionnaire.setIsStar(isStar);
            questionnaire.setStatus(status);
            questionnaire.setStartTime(startTime);
            questionnaire.setEndTime(endTime);
            questionnaire.setIsRepeat(isRepeat);
            questionnaire.setIsLogin(isLogin);
            questionnaire.setIsAppoint(isAppoint);
            questionnaire.setReleaseTime(null);
            questionnaire.setCode(null);
            questionnaire.setIsNumber(isNumber);
            questionnaire.setType(type1);
            questionnaire.setIsLimit(isLimit);
            if (isLimit == 1) {
                questionnaire.setAllCount(allCount);
                questionnaire.setNowCount(nowCount);
            }
            questionnaire.setIsOrder(isOrder);
            UpdateWrapper<Questionnaire> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            questionnaireMapper.update(questionnaire, wrapper);
            // 删除所有问题选项和答卷
            submitRecordMapper.delete(new QueryWrapper<SubmitRecord>().eq("questionnaire_id", id));
            examMapper.delete(new QueryWrapper<Exam>().eq("questionnaire_id", id));
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("questionnaire_id", id);
            List<Question> questions = questionMapper.selectList(queryWrapper);
            for (Question question : questions) {
                Integer questionId = question.getId();
                submitContentMapper.delete(new QueryWrapper<SubmitContent>().eq("question_id", questionId));
                Integer type = question.getType();
                if (type == 0 || type == 1) { // 单选或多选
                    choiceMapper.delete(new QueryWrapper<Choice>().eq("question_id", questionId));
                } else if (type == 2) { // 评分
                    scoreMapper.delete(new QueryWrapper<Score>().eq("question_id", questionId));
                }
                questionMapper.delete(new QueryWrapper<Question>().eq("id", questionId));
            }
            // 创建新问卷
            for (QuestionDTO questionDTO : questionDTOS) {
                Question question = new Question();
                question.setQuestionnaireId(id);
                question.setType(questionDTO.getType());
                question.setContent(questionDTO.getContent());
                question.setIsCompulsory(questionDTO.getIsCompulsory());
                question.setIntroduction(questionDTO.getIntroduction());
                if (questionDTO.getType() == 2 || questionDTO.getType() == 4) {
                    question.setMaxScore(questionDTO.getMax());
                }
                if (questionDTO.getType() == 5 || questionDTO.getType() == 6) {
                    question.setIsShowVotes(questionDTO.getIsShowVotes());
                    question.setIsShowPercentage(questionDTO.getIsShowPercentage());
                }
                if (type1 == 3) {
                    if (questionDTO.getScore() != null) {
                        question.setScore(questionDTO.getScore());
                    }
                    if (question.getType() == 3) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            question.setAnswer(questionDTO.getAnswer());
                        }
                    }
                    if (question.getType() == 4) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            String[] split = questionDTO.getAnswer().split(";");
                            if (split.length != 0) {
                                question.setAnswer(questionDTO.getAnswer());
                            }
                        }
                    }
                    if (questionDTO.getAnalysis() != null) {
                        question.setAnalysis(questionDTO.getAnalysis());
                    }
                }
                questionMapper.insert(question);
                Integer questionId = question.getId();
                if (questionDTO.getType() == 0 || questionDTO.getType() == 1 || questionDTO.getType() == 5 ||
                        questionDTO.getType() == 6) {
                    List<ChoiceDTO> choiceDTOS = questionDTO.getChoices();
                    for (ChoiceDTO choiceDTO : choiceDTOS) {
                        Choice choice = new Choice();
                        choice.setQuestionId(questionId);
                        choice.setChoiceId(choiceDTO.getChoiceId());
                        choice.setChoiceContent(choiceDTO.getChoiceContent());
                        if (choiceDTO.getIsLimit() == null) {
                            choice.setIsLimit(0);
                        } else {
                            choice.setIsLimit(choiceDTO.getIsLimit());
                        }
                        if (choice.getIsLimit() == 1) {
                            choice.setAllCount(choiceDTO.getAllCount());
                            choice.setNowCount(choiceDTO.getAllCount());
                        }
                        if (type1 == 3) {
                            if (choiceDTO.getIsAnswer() == null) {
                                choice.setIsAnswer(0);
                            } else {
                                choice.setIsAnswer(choiceDTO.getIsAnswer());
                            }
                        }
                        choiceMapper.insert(choice);
                    }
                } else if (questionDTO.getType() == 2) {
                    List<String> texts = questionDTO.getTexts();
                    Integer i = 1;
                    for (String text : texts) {
                        Score score = new Score();
                        score.setScoreId(i);
                        score.setQuestionId(questionId);
                        score.setScoreContent(text);
                        scoreMapper.insert(score);
                        i += 1;
                    }
                }
            }
            return ReturnResponse.OK(id + "");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<Page<QuestionnaireInfVO>> getRecycleQuestionnaireInf(QuestionnaireInfScreenDTO questionnaireInfScreenDTO) {
        Integer userId = questionnaireInfScreenDTO.getUserId();
        Integer type = questionnaireInfScreenDTO.getType() == null ? (Integer) 0 : questionnaireInfScreenDTO.getType();
        String keyword = questionnaireInfScreenDTO.getKeyword();
        Integer status = questionnaireInfScreenDTO.getStatus();
        Integer isStar = questionnaireInfScreenDTO.getIsStar();
        Integer current = questionnaireInfScreenDTO.getCurrent();
        Integer size = questionnaireInfScreenDTO.getSize();
        try {
            QueryWrapper<Questionnaire> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("status", 3);
            if (keyword != null) {
                queryWrapper.like("name", keyword);
            }
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            if (isStar != null) {
                queryWrapper.eq("is_star", isStar);
            }
            switch (type) {
                case 0:
                    queryWrapper.orderByDesc("update_time");
                    break;
                case 1:
                    queryWrapper.orderByAsc("update_time");
                    break;
                case 2:
                    queryWrapper.orderByDesc("create_time");
                    break;
                case 3:
                    queryWrapper.orderByAsc("create_time");
                    break;
                case 4:
                    queryWrapper.orderByDesc("count");
                    break;
                case 5:
                    queryWrapper.orderByAsc("count");
                    break;
            }
            Page<QuestionnaireInfVO> page = new Page<>();
            page.setCurrent(current);
            page.setSize(size);
            Page<QuestionnaireInfVO> questionnaireInfVOS = questionnaireMapper.getQuestionnaireInf(page, queryWrapper);
            return ReturnResponse.OK(questionnaireInfVOS);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }


    @Override
    public ReturnResponse<String> recycleDelete(Integer userId) {
        try {
            QueryWrapper<Questionnaire> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("user_id", userId);
//            wrapper1.eq("user_id",1);
            wrapper1.eq("status", 3);
            List<Questionnaire> questionnaires = questionnaireMapper.selectList(wrapper1);
            questionnaireMapper.delete(wrapper1);
            for (Questionnaire questionnaire : questionnaires) {

                QueryWrapper<Question> wrapper = new QueryWrapper<>();
                Integer questionnaireId = questionnaire.getId();
                wrapper.eq("questionnaire_id", questionnaireId);
                QueryWrapper<Exam> wrapper10 = new QueryWrapper<>();
                wrapper10.eq("questionnaire_id",questionnaireId);
                examMapper.delete(wrapper10);
                List<Question> questionList = questionMapper.selectList(wrapper);
                List<Integer> questionIdList = new ArrayList<>();
                for (Question question : questionList) {
                    questionIdList.add(question.getId());
                }
                questionMapper.delete(wrapper);
                QueryWrapper<Questionnaire> wrapper5 = new QueryWrapper<>();
                wrapper5.eq("id", questionnaireId);
                questionnaireMapper.delete(wrapper5);
                for (Integer questionId : questionIdList) {
                    QueryWrapper<Choice> wrapper2 = new QueryWrapper<>();
                    wrapper2.eq("question_id", questionId);
                    choiceMapper.delete(wrapper2);
                    QueryWrapper<SubmitContent> wrapper3 = new QueryWrapper<>();
                    wrapper3.eq("question_id", questionId);
                    submitContentMapper.delete(wrapper3);
                    QueryWrapper<Score> wrapper7 = new QueryWrapper<>();
                    wrapper7.eq("question_id", questionId);
                    scoreMapper.delete(wrapper7);
                }
                QueryWrapper<SubmitRecord> wrapper4 = new QueryWrapper<>();
                wrapper4.eq("questionnaire_id", questionnaireId);
                submitRecordMapper.delete(wrapper4);

            }
            return ReturnResponse.OK("删除成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> deleteRecycleQuestionnaire(DeleteRecycleDTO deleteRecycleDTO) {
        try {
            QueryWrapper<Question> wrapper = new QueryWrapper<>();
            Integer questionnaireId = deleteRecycleDTO.getQuestionnaireId();
            wrapper.eq("questionnaire_id", questionnaireId);
            QueryWrapper<Exam> wrapper10 = new QueryWrapper<>();
            wrapper10.eq("questionnaire_id",questionnaireId);
            examMapper.delete(wrapper10);
            List<Question> questionList = questionMapper.selectList(wrapper);
            List<Integer> questionIdList = new ArrayList<>();
            for (Question question : questionList) {
                questionIdList.add(question.getId());
            }
            questionMapper.delete(wrapper);
            QueryWrapper<Questionnaire> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("id", questionnaireId);
            questionnaireMapper.delete(wrapper1);
            for (Integer questionId : questionIdList) {
                QueryWrapper<Choice> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("question_id", questionId);
                choiceMapper.delete(wrapper2);
                QueryWrapper<SubmitContent> wrapper3 = new QueryWrapper<>();
                wrapper3.eq("question_id", questionId);
                submitContentMapper.delete(wrapper3);
                QueryWrapper<Score> wrapper7 = new QueryWrapper<>();
                wrapper7.eq("question_id", questionId);
                scoreMapper.delete(wrapper7);
            }
            QueryWrapper<SubmitRecord> wrapper4 = new QueryWrapper<>();
            wrapper4.eq("questionnaire_id", questionnaireId);
            submitRecordMapper.delete(wrapper4);
            return ReturnResponse.OK("删除成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> recoverDelete(DeleteRecycleDTO deleteRecycleDTO) {
        try {
            QueryWrapper<Questionnaire> wrapper = new QueryWrapper<>();
            Integer questionnaireId = deleteRecycleDTO.getQuestionnaireId();
            wrapper.eq("id", questionnaireId);
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setStatus(0);
            questionnaireMapper.update(questionnaire, wrapper);
            return ReturnResponse.OK("恢复成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> emptyData(DeleteRecycleDTO deleteRecycleDTO) {
        try {
            QueryWrapper<Questionnaire> wrapper = new QueryWrapper<>();
            Integer questionnaireId = deleteRecycleDTO.getQuestionnaireId();
            wrapper.eq("id", questionnaireId);
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setCount(0);
            questionnaireMapper.update(questionnaire, wrapper);
            QueryWrapper<Question> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("questionnaire_id", questionnaireId);
            QueryWrapper<Exam> wrapper10 = new QueryWrapper<>();
            wrapper10.eq("questionnaire_id",questionnaireId);
            examMapper.delete(wrapper10);
            List<Question> questionList = questionMapper.selectList(wrapper1);
            List<Integer> questionIdList = new ArrayList<>();
            for (Question question : questionList) {
                questionIdList.add(question.getId());
            }
            questionMapper.delete(wrapper1);
            for (Integer questionId : questionIdList) {
                QueryWrapper<Choice> wrapper3 = new QueryWrapper<>();
                wrapper3.eq("question_id", questionId);
                choiceMapper.delete(wrapper3);
                QueryWrapper<SubmitContent> wrapper4 = new QueryWrapper<>();
                wrapper4.eq("question_id", questionId);
                submitContentMapper.delete(wrapper4);
                QueryWrapper<Score> wrapper7 = new QueryWrapper<>();
                wrapper7.eq("question_id", questionId);
                scoreMapper.delete(wrapper7);
            }
            QueryWrapper<SubmitRecord> wrapper5 = new QueryWrapper<>();
            wrapper5.eq("questionnaire_id", questionnaireId);
            submitRecordMapper.delete(wrapper5);
            return ReturnResponse.OK("清空数据成功");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> editQuestionnaireCopy(QuestionnaireDTO questionnaireDTO) {
        Integer userId = questionnaireDTO.getUserId();
        String name = questionnaireDTO.getName();
        String introduction = questionnaireDTO.getIntroduction();
        Integer count = 0;
        Integer isStar = 0;
        Integer status = 0;
        LocalDateTime startTime = questionnaireDTO.getStartTime();
        LocalDateTime endTime = questionnaireDTO.getEndTime();
        Integer isRepeat = questionnaireDTO.getIsRepeat();
        Integer isLogin = questionnaireDTO.getIsLogin();
        Integer isAppoint = questionnaireDTO.getIsAppoint();
        Integer isNumber = questionnaireDTO.getIsNumber();
        Integer type = questionnaireDTO.getType(); // 0-普通 1-投票 2-报名 3-考试 4-疫情
        Integer isLimit;
        Integer allCount = questionnaireDTO.getAllCount();
        Integer nowCount = questionnaireDTO.getAllCount();
        Integer isOrder;
        if (questionnaireDTO.getIsLimit() == null) {
            isLimit = 0;
        } else {
            isLimit = questionnaireDTO.getIsLimit();
        }
        if (questionnaireDTO.getIsOrder() == null) {
            isOrder = 0;
        } else {
            isOrder = questionnaireDTO.getIsOrder();
        }
        List<QuestionDTO> questionDTOS = questionnaireDTO.getQuestions();
        try {
            if (endTime == null) {
                endTime = LocalDateTime.of(2099, 12, 31, 23, 59, 59);
            }
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setUserId(userId);
            questionnaire.setName(name);
            questionnaire.setIntroduction(introduction);
            questionnaire.setCount(count);
            questionnaire.setIsStar(isStar);
            questionnaire.setStatus(status);
            questionnaire.setStartTime(startTime);
            questionnaire.setEndTime(endTime);
            questionnaire.setIsRepeat(isRepeat);
            questionnaire.setIsLogin(isLogin);
            questionnaire.setIsAppoint(isAppoint);
            questionnaire.setReleaseTime(null);
            questionnaire.setCode(null);
            questionnaire.setIsNumber(isNumber);
            questionnaire.setType(type);
            questionnaire.setIsLimit(isLimit);
            if (isLimit == 1) {
                questionnaire.setAllCount(allCount);
                questionnaire.setNowCount(nowCount);
            }
            questionnaire.setIsOrder(isOrder);
            questionnaireMapper.insert(questionnaire);
            Integer questionnaireId = questionnaire.getId();
            for (QuestionDTO questionDTO : questionDTOS) {
                Question question = new Question();
                question.setQuestionnaireId(questionnaireId);
                question.setType(questionDTO.getType());
                question.setContent(questionDTO.getContent());
                question.setIsCompulsory(questionDTO.getIsCompulsory());
                question.setIntroduction(questionDTO.getIntroduction());
                if (questionDTO.getType() == 2 || questionDTO.getType() == 4) { // 多项填空将空格数存在max_score
                    question.setMaxScore(questionDTO.getMax());
                }
                // 5-投票单选 6-投票多选
                if (questionDTO.getType() == 5 || questionDTO.getType() == 6) {
                    question.setIsShowVotes(questionDTO.getIsShowVotes());
                    question.setIsShowPercentage(questionDTO.getIsShowPercentage());
                }
                if (type == 3) {
                    if (questionDTO.getScore() != null) {
                        question.setScore(questionDTO.getScore());
                    }
                    if (question.getType() == 3) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            question.setAnswer(questionDTO.getAnswer());
                        }
                    }
                    if (question.getType() == 4) {
                        if (questionDTO.getAnswer() != null && !questionDTO.getAnswer().equals("")) {
                            String[] split = questionDTO.getAnswer().split(";");
                            if (split.length != 0) {
                                question.setAnswer(questionDTO.getAnswer());
                            }
                        }
                    }
                    if (questionDTO.getAnalysis() != null) {
                        question.setAnalysis(questionDTO.getAnalysis());
                    }
                }
                questionMapper.insert(question);
                Integer questionId = question.getId();
                if (questionDTO.getType() == 0 || questionDTO.getType() == 1 || questionDTO.getType() == 5 ||
                        questionDTO.getType() == 6) {
                    List<ChoiceDTO> choiceDTOS = questionDTO.getChoices();
                    for (ChoiceDTO choiceDTO : choiceDTOS) {
                        Choice choice = new Choice();
                        choice.setQuestionId(questionId);
                        choice.setChoiceId(choiceDTO.getChoiceId());
                        choice.setChoiceContent(choiceDTO.getChoiceContent());
                        if (choiceDTO.getIsLimit() == null) {
                            choice.setIsLimit(0);
                        } else {
                            choice.setIsLimit(choiceDTO.getIsLimit());
                        }
                        if (choice.getIsLimit() == 1) {
                            choice.setAllCount(choiceDTO.getAllCount());
                            choice.setNowCount(choiceDTO.getAllCount());
                        }
                        if (type == 3) {
                            if (choiceDTO.getIsAnswer() == null) {
                                choice.setIsAnswer(0);
                            } else {
                                choice.setIsAnswer(choiceDTO.getIsAnswer());
                            }
                        }
                        choiceMapper.insert(choice);
                    }
                } else if (questionDTO.getType() == 2) {
                    List<String> texts = questionDTO.getTexts();
                    Integer i = 1;
                    for (String scoreContent : texts) {
                        Score score = new Score();
                        score.setScoreId(i);
                        score.setQuestionId(questionId);
                        score.setScoreContent(scoreContent);
                        scoreMapper.insert(score);
                        i += 1;
                    }
                }
            }
            return ReturnResponse.OK("" + questionnaireId);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> getCode(Integer questionnaireId) {
        try {
            return ReturnResponse.OK(questionnaireMapper.selectById(questionnaireId).getCode());
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse getQuestionnaireAnswerAnalysis(Integer questionnaireId, Integer userId) {
        try {
            if (!userId.equals(questionnaireMapper.selectById(questionnaireId).getUserId())) { // 该问卷不是当前登录用户创建的
                return ReturnResponse.packageObject("无访问权限", ReturnStatus.FAILURE);
            }
            AnswerAnalysisVO answerAnalysisVO = new AnswerAnalysisVO();
            List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>()
                    .eq("questionnaire_id", questionnaireId));
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            Integer isNumber = questionnaire.getIsNumber();
            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);
                QuestionContentVO questionContentVO = new QuestionContentVO();
                questionContentVO.setId(i + 1);
                if (isNumber == 0) {
                    int j = i + 1;
                    questionContentVO.setContent(j + "." + question.getContent());
                } else {
                    questionContentVO.setContent(question.getContent());
                }
                if (questionnaire.getType() == 3) { // 考试问卷返回正确答案
                    if (question.getType() == 0 || question.getType() == 1) {
                        List<Choice> choices = choiceMapper.selectList(new QueryWrapper<Choice>()
                                .eq("question_id", question.getId()));
                        for (int j = 0; j < choices.size(); j++) {
                            if (choices.get(j).getIsAnswer() == 1) {
                                questionContentVO.getCorrectAnswer().add(j + 1 + "");
                            }
                        }

                    } else if (question.getType() == 3) {
                        if (question.getAnswer() != null) {
                            questionContentVO.getCorrectAnswer().add(question.getAnswer());
                        }
                    } else if (question.getType() == 4) {
                        if (question.getAnswer() != null) {
                            String[] split = question.getAnswer().split(";");
                            for (String s : split) {
                                questionContentVO.getCorrectAnswer().add(s);
                            }
                        }
                    }
                }
                answerAnalysisVO.getQuestionContentVOS().add(questionContentVO);
            }

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            List<SubmitRecord> submitRecords = submitRecordMapper.selectList(new QueryWrapper<SubmitRecord>()
                    .eq("questionnaire_id", questionnaireId));
            for (Question question : questionList) {
                Integer id = question.getId(); // 题号
                Integer type = question.getType();
                AnswerResultVO answerResultVO = new AnswerResultVO();
                answerResultVO.setType(type);
                answerResultVO.setQuestionId(id); // 返回题号
                if (type == 0 || type == 5) { // 单选题
                    // 插入选项
                    List<ChoiceVO> choiceVOS = choiceMapper.getChoiceByQuestionId(new QueryWrapper<Choice>().eq("question_id", id));
                    for (ChoiceVO choiceVO : choiceVOS) {
                        ChoiceAnswerVO choiceAnswerVO = new ChoiceAnswerVO();
                        choiceAnswerVO.setId(choiceVO.getChoiceId());
                        choiceAnswerVO.setContent(choiceVO.getChoiceContent());
                        choiceAnswerVO.setCount(0);
                        answerResultVO.getChoiceAnswerVOS().add(choiceAnswerVO);
                    }
                    // 插入人数
                    Integer correctCount = 0;
                    for (SubmitRecord submitRecord : submitRecords) {
                        Integer submitRecordId = submitRecord.getId();
                        List<SubmitContent> submitContents = submitContentMapper.selectList(new QueryWrapper<SubmitContent>()
                                .eq("submit_record_id", submitRecordId).eq("question_id", id));
                        for (SubmitContent submitContent : submitContents) {
                            String content = submitContent.getContent();
                            if (!content.equals(" ")) {
                                Integer idx = Integer.parseInt(content);
                                Integer count = answerResultVO.getChoiceAnswerVOS().get(idx - 1).getCount();
                                answerResultVO.getChoiceAnswerVOS().get(idx - 1).setCount(count + 1);
                            }
                            if (questionnaire.getType() == 3) {
                                if (submitContent.getScore() != null && submitContent.getScore().equals(question.getScore())) {
                                    correctCount++;
                                }
                            }
                        }
                    }
                    // 插入百分数
                    Integer sum = 0;
                    for (ChoiceAnswerVO choiceAnswerVO : answerResultVO.getChoiceAnswerVOS()) {
                        sum += choiceAnswerVO.getCount();
                    }
                    answerResultVO.setSum(sum);
                    answerResultVO.setCorrectRate(numberFormat.format((double) correctCount / (double) sum * 100) + "%");
                    if (sum == 0) {
                        for (ChoiceAnswerVO choiceAnswerVO : answerResultVO.getChoiceAnswerVOS()) {
                            choiceAnswerVO.setPercentage("0%");
                        }
                    } else {
                        for (ChoiceAnswerVO choiceAnswerVO : answerResultVO.getChoiceAnswerVOS()) {
                            String result = numberFormat.format((double) choiceAnswerVO.getCount() / (double) sum * 100);
                            choiceAnswerVO.setPercentage(result + "%");
                        }
                    }
                } else if (type == 1 || type == 6) { // 多选题
                    // 插入选项
                    List<ChoiceVO> choiceVOS = choiceMapper.getChoiceByQuestionId(new QueryWrapper<Choice>().eq("question_id", id));
                    for (ChoiceVO choiceVO : choiceVOS) {
                        ChoiceAnswerVO choiceAnswerVO = new ChoiceAnswerVO();
                        choiceAnswerVO.setId(choiceVO.getChoiceId());
                        choiceAnswerVO.setContent(choiceVO.getChoiceContent());
                        choiceAnswerVO.setCount(0);
                        answerResultVO.getChoiceAnswerVOS().add(choiceAnswerVO);
                    }
                    // 插入人数
                    Integer correctCount = 0;
                    Integer sum = 0; // 多选题以有效答卷数为分母
                    for (SubmitRecord submitRecord : submitRecords) {
                        Integer submitRecordId = submitRecord.getId();
                        List<SubmitContent> submitContents = submitContentMapper.selectList(new QueryWrapper<SubmitContent>()
                                .eq("submit_record_id", submitRecordId).eq("question_id", id));
                        for (SubmitContent submitContent : submitContents) {
                            String content = submitContent.getContent();
                            if (!content.equals(" ")) {
                                Integer idx = Integer.parseInt(content);
                                Integer count = answerResultVO.getChoiceAnswerVOS().get(idx - 1).getCount();
                                answerResultVO.getChoiceAnswerVOS().get(idx - 1).setCount(count + 1);
                            }
                            if (questionnaire.getType() == 3) {
                                if (submitContent.getScore() != null && submitContent.getScore().equals(question.getScore())) {
                                    correctCount++;
                                }
                            }
                        }
                        sum++;
                    }
                    answerResultVO.setSum(sum);
                    answerResultVO.setCorrectRate(numberFormat.format((double) correctCount / (double) sum * 100) + "%");
                    // 插入百分数
                    if (sum == 0) {
                        for (ChoiceAnswerVO choiceAnswerVO : answerResultVO.getChoiceAnswerVOS()) {
                            choiceAnswerVO.setPercentage("0%");
                        }
                    } else {
                        for (ChoiceAnswerVO choiceAnswerVO : answerResultVO.getChoiceAnswerVOS()) {
                            String result = numberFormat.format((double) choiceAnswerVO.getCount() / (double) sum * 100);
                            choiceAnswerVO.setPercentage(result + "%");
                        }
                    }
                } else if(type == 2) { // 评分题
                    // 插入星级
                    List<ScoreVO> scoreVOS = scoreMapper.getScoreByQuestionId(new QueryWrapper<Score>().eq("question_id", id));
                    for (ScoreVO scoreVO : scoreVOS) {
                        ScoreAnswerVO scoreAnswerVO = new ScoreAnswerVO();
                        scoreAnswerVO.setId(scoreVO.getScoreId());
                        scoreAnswerVO.setContent(scoreVO.getScoreContent());
                        scoreAnswerVO.setCount(0);
                        answerResultVO.getScoreAnswerVOS().add(scoreAnswerVO);
                    }
                    // 插入人数
                    for (SubmitRecord submitRecord : submitRecords) {
                        Integer submitRecordId = submitRecord.getId();
                        List<SubmitContent> submitContents = submitContentMapper.selectList(new QueryWrapper<SubmitContent>()
                                .eq("submit_record_id", submitRecordId).eq("question_id", id));
                        for (SubmitContent submitContent : submitContents) {
                            String content = submitContent.getContent();
                            if (!content.equals(" ")) {
                                Integer idx = Integer.parseInt(content);
                                Integer count = answerResultVO.getScoreAnswerVOS().get(idx - 1).getCount();
                                answerResultVO.getScoreAnswerVOS().get(idx - 1).setCount(count + 1);
                            }
                        }
                    }
                    // 插入百分数
                    Integer sum = 0;
                    for (ScoreAnswerVO scoreAnswerVO : answerResultVO.getScoreAnswerVOS()) {
                        sum += scoreAnswerVO.getCount();
                    }
                    answerResultVO.setSum(sum);
                    if (sum == 0) {
                        for (ScoreAnswerVO scoreAnswerVO : answerResultVO.getScoreAnswerVOS()) {
                            scoreAnswerVO.setPercentage("0%");
                        }
                    } else {
                        for (ScoreAnswerVO scoreAnswerVO : answerResultVO.getScoreAnswerVOS()) {
                            String result = numberFormat.format((double) scoreAnswerVO.getCount() / (double) sum * 100);
                            scoreAnswerVO.setPercentage(result + "%");
                        }
                    }
//                } else { // 填空题

                }
                answerAnalysisVO.getAnswerResultVOS().add(answerResultVO);
            }

            // 考试分析
            if (questionnaire.getType() == 3) {
                ExamAnalysisVO examAnalysisVO = new ExamAnalysisVO();
                Double max = -10d;
                Double min = 110d;
                Double scoreSum = 0d;
                for (SubmitRecord submitRecord : submitRecords) {
                    if (submitRecord.getScore() != null) {
                        max = Math.max(max, submitRecord.getScore());
                        min = Math.min(min, submitRecord.getScore());
                        scoreSum += submitRecord.getScore();
                    }
                }
                if (max == -10 && min == 110) { // 没有分数
                    examAnalysisVO.setMax(0d);
                    examAnalysisVO.setMin(0d);
                    examAnalysisVO.setAverage(0d);
                } else {
                    examAnalysisVO.setMax(max);
                    examAnalysisVO.setMin(min);
                    examAnalysisVO.setAverage(scoreSum / (double) submitRecords.size());
                }
                answerAnalysisVO.setExamAnalysisVO(examAnalysisVO);
            }

            return ReturnResponse.OK(answerAnalysisVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<CompletionDetailVO> getCompletionAnswer(Integer questionId) {
        try {
            CompletionDetailVO completionDetailVO = new CompletionDetailVO();
            Question question = questionMapper.selectById(questionId);
            Integer max;
            if (question.getType() == 3 || question.getType() == 7) {
                max = 1;
            } else {
                max = questionMapper.selectById(questionId).getMaxScore();
            }
            completionDetailVO.setMax(max);
            List<ContentUpdateTimeVO> contentUpdateTimeVOS = submitContentMapper.getContentTimeByQuestionId(new QueryWrapper<SubmitContent>().eq("question_id", questionId));
            for (int i = 0; i < contentUpdateTimeVOS.size(); i++) {
                if (i % max == 0) {
                    completionDetailVO.getSubmitTimeList().add(contentUpdateTimeVOS.get(i).getUpdateTime());
                }
            }
            for (int i = 0; i < max; i++) {
                completionDetailVO.getContent().add(new ArrayList<>());
            }
            int m = 0;
            while (m < max) {
                for (int i = 0; i < contentUpdateTimeVOS.size(); i++) {
                    if (i % max == m) {
                        completionDetailVO.getContent().get(m).add(contentUpdateTimeVOS.get(i).getContent());
                    }
                }
                m++;
            }
            return ReturnResponse.OK(completionDetailVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<VotesResultVO> getVotesResult(Integer questionnaireId) {
        try {
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            VotesResultVO votesResultVO = new VotesResultVO();
            votesResultVO.setName(questionnaire.getName());
            List<Question> questions = questionMapper.selectList(new QueryWrapper<Question>().eq("questionnaire_id", questionnaireId));
            for (Question question : questions) {
                if (question.getType() == 5 || question.getType() == 6) {
                    VotesQuestionVO votesQuestionVO = new VotesQuestionVO();
                    votesQuestionVO.setContent(question.getContent());
                    List<Choice> choices = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", question.getId()));
                    List<VotesChoiceVO> votesChoiceVOS = new ArrayList<>();
                    for (Choice choice : choices) {
                        VotesChoiceVO votesChoiceVO = new VotesChoiceVO();
                        votesChoiceVO.setChoiceContent(choice.getChoiceContent());
                        Integer votes = submitContentMapper.selectCount(new QueryWrapper<SubmitContent>().eq("question_id", question.getId())
                                .eq("content", "" + choice.getChoiceId()));
                        Integer counts = submitRecordMapper.selectCount(new QueryWrapper<SubmitRecord>().eq("questionnaire_id", questionnaireId));
                        votesChoiceVO.setVotes(votes);
                        if (counts == 0) {
                            votesChoiceVO.setPercentage(0.0);
                        } else {
                            votesChoiceVO.setPercentage(Double.parseDouble(String.format("%.1f", (double) votes / counts * 100.0)));
                        }

                        votesChoiceVOS.add(votesChoiceVO);
                    }
                    votesQuestionVO.sort(votesChoiceVOS);
                    int nowRanking = 0;
                    int lastVotes = -1;
                    int i = 1;
                    for (VotesChoiceVO votesChoiceVO : votesChoiceVOS) {
                        if (votesChoiceVO.getVotes() == lastVotes) {
                            votesChoiceVO.setRanking(nowRanking);
                        } else {
                            votesChoiceVO.setRanking(i);
                            nowRanking = i;
                        }
                        lastVotes = votesChoiceVO.getVotes();
                        i += 1;
                    }
                    votesQuestionVO.setVotesChoiceVOS(votesChoiceVOS);
                    votesResultVO.getVotesQuestionVOS().add(votesQuestionVO);
                }
            }
            return ReturnResponse.OK(votesResultVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse getExamByCode(CheckExamDTO checkExamDTO) {
        try {
            Integer questionnaireId = checkExamDTO.getQuestionnaireId();
            String ip = checkExamDTO.getIp(); // ip
            LocalDateTime startTime = questionnaireMapper.selectById(questionnaireId).getStartTime();
            if (startTime != null && LocalDateTime.now().isBefore(startTime)) {
                return ReturnResponse.packageObject("考试还未开始", ReturnStatus.FAILURE);
            }
            if (LocalDateTime.now().isAfter(questionnaireMapper.selectById(questionnaireId).getEndTime())) {
                return ReturnResponse.packageObject("考试已结束", ReturnStatus.FAILURE);
            }
            // 返回问卷内容
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            QuestionnaireVO questionnaireVO = new QuestionnaireVO();
            questionnaireVO.setName(questionnaire.getName());
            questionnaireVO.setIntroduction(questionnaire.getIntroduction());
            questionnaireVO.setStartTime(questionnaire.getStartTime());
            questionnaireVO.setEndTime(questionnaire.getEndTime());
            questionnaireVO.setIsRepeat(questionnaire.getIsRepeat());
            questionnaireVO.setIsLogin(questionnaire.getIsLogin());
            questionnaireVO.setIsAppoint(questionnaire.getIsAppoint());
            questionnaireVO.setIsNumber(questionnaire.getIsNumber());
            questionnaireVO.setType(questionnaire.getType());
            questionnaireVO.setIsLimit(questionnaire.getIsLimit());
            if (questionnaire.getIsLimit() == 1) {
                questionnaireVO.setAllCount(questionnaire.getAllCount());
            } else {
                questionnaireVO.setAllCount(100);
            }
            questionnaireVO.setIsOrder(questionnaire.getIsOrder());

            // 考试问卷题目初始顺序
            List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("questionnaire_id", questionnaireId));
            // 考试问卷实际答题顺序
            List<Question> trueQuestionList = new ArrayList<>();
            if (questionnaire.getIsOrder() == 0) {
                trueQuestionList.addAll(questionList);
            } else if (questionnaire.getIsOrder() == 1) { // 乱序
                // 乱序获取题目并储存该顺序
                List<Exam> exams = examMapper.selectList(new QueryWrapper<Exam>()
                        .eq("ip", ip).eq("questionnaire_id", questionnaireId));
                if (!exams.isEmpty()) { // 打开过问卷，返回之前的题目顺序
                    for (Exam exam : exams) {
                        trueQuestionList.add(questionMapper.selectById(exam.getQuestionId()));
                    }
                } else { // 乱序获取题目并保存该顺序
                    // 为了保证姓名和学号不会被打乱，所以从第三题开始打乱。
                    if (questionList.size() > 1) {
                        trueQuestionList.add(questionList.get(0));
                        questionList.remove(0);
                    }
                    if (questionList.size() > 1) {
                        trueQuestionList.add(questionList.get(0));
                        questionList.remove(0);
                    }
                    Collections.shuffle(questionList);
                    trueQuestionList.addAll(questionList);
                    for (Question question : trueQuestionList) {
                        Exam exam = new Exam();
                        exam.setIp(ip);
                        exam.setQuestionnaireId(questionnaireId);
                        exam.setQuestionId(question.getId());
                        examMapper.insert(exam);
                    }
                }
            }

            // 返回考试题
            for (Question question : trueQuestionList) {
                QuestionVO questionVO = new QuestionVO();
                questionVO.setQuestionId(question.getId());
                questionVO.setType(question.getType());
                questionVO.setContent(question.getContent());
                questionVO.setIsCompulsory(question.getIsCompulsory());
                questionVO.setIntroduction(question.getIntroduction());
                if (question.getType() == 2 || question.getType() == 4) { // 多项填空将空格数存在max_score
                    questionVO.setMax(question.getMaxScore());
                }
                if (questionnaire.getType() == 3) {
                    questionVO.setScore(question.getScore()); // 考试题目分值
                }
                List<Choice> choiceList = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", question.getId()));
                for (Choice choice : choiceList) {
                    ChoiceVO choiceVO = new ChoiceVO();
                    choiceVO.setChoiceId(choice.getChoiceId());
                    choiceVO.setChoiceContent(choice.getChoiceContent());
//                    choiceVO.setIsAnswer(choice.getIsAnswer());
                    questionVO.getChoices().add(choiceVO);
                }
                questionVO.setTexts(scoreMapper.getScoreContents(new QueryWrapper<Score>().eq("question_id", question.getId())));
                questionnaireVO.getQuestions().add(questionVO);
            }
            return ReturnResponse.OK(questionnaireVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse getCrossAnalysis(AnalysisDTO analysisDTO) {
        try {
            Integer independentVariableId = analysisDTO.getIndependentVariableId();
            Integer dependentVariableId = analysisDTO.getDependentVariableId();
            if (!analysisDTO.getUserId().equals(questionnaireMapper.selectById(questionMapper.selectById(independentVariableId).getQuestionnaireId()).getUserId())) {
                return ReturnResponse.packageObject("无访问权限", ReturnStatus.FAILURE);
            }
            Question question1 = questionMapper.selectById(independentVariableId);
            Question question2 = questionMapper.selectById(dependentVariableId);
            AnswerAnalysisVO answerAnalysisVO = new AnswerAnalysisVO();
            List<Choice> choices;
            List<Score> scores;
            List<Choice> choiceList;
            List<Score> scoreList;
            if (question1.getType() == 0 || question1.getType() == 1 || question1.getType() == 5 || question1.getType() == 6) {
                choices = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", independentVariableId));
                for (Choice choice : choices) {
                    QuestionContentVO questionContentVO = new QuestionContentVO();
                    questionContentVO.setId(choice.getChoiceId());
                    questionContentVO.setContent(choice.getChoiceContent());
                    answerAnalysisVO.getQuestionContentVOS().add(questionContentVO);
                }
                for (int i = 0; i < choices.size(); i++) {
//                    int sum = submitRecordMapper.selectCount(new QueryWrapper<SubmitRecord>().eq("questionnaire_id", question1.getQuestionnaireId()));
                    int sum = submitContentMapper.selectCount(new QueryWrapper<SubmitContent>().eq("question_id", independentVariableId).eq("content", String.valueOf(i + 1)));
                    AnswerResultVO answerResultVO = new AnswerResultVO();
                    answerResultVO.setQuestionId(dependentVariableId);
                    answerResultVO.setType(question2.getType());
                    answerResultVO.setSum(sum);
                    if (question2.getType() == 0 || question2.getType() == 1 || question1.getType() == 5 || question1.getType() == 6) {
                        choiceList = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", dependentVariableId));
                        List<ChoiceAnswerVO> choiceAnswerVOS = new ArrayList<>();
                        for (Choice choice : choiceList) {
                            ChoiceAnswerVO choiceAnswerVO = new ChoiceAnswerVO();
                            choiceAnswerVO.setId(choice.getChoiceId());
                            QueryWrapper<SubmitContent> queryWrapper = new QueryWrapper<>();
                            queryWrapper
                                    .eq("A.question_id", independentVariableId)
                                    .eq("B.question_id", dependentVariableId)
                                    .eq("A.content", String.valueOf(i + 1))
                                    .eq("B.content", String.valueOf(choice.getChoiceId()));
                            int count = submitContentMapper.getCountByCrossAnalysis(queryWrapper);
                            choiceAnswerVO.setCount(count);
                            choiceAnswerVO.setContent(choice.getChoiceContent());
                            if (sum == 0) {
                                choiceAnswerVO.setPercentage("0.0%");
                            } else {
                                choiceAnswerVO.setPercentage(String.format("%.1f", (double) count / sum * 100) + "%");
                            }
                            choiceAnswerVOS.add(choiceAnswerVO);
                        }
                        answerResultVO.setChoiceAnswerVOS(choiceAnswerVOS);
                    } else if (question2.getType() == 2) {
                        scoreList = scoreMapper.selectList(new QueryWrapper<Score>().eq("question_id", dependentVariableId));
                        List<ScoreAnswerVO> scoreAnswerVOS = new ArrayList<>();
                        for (Score score : scoreList) {
                            ScoreAnswerVO scoreAnswerVO = new ScoreAnswerVO();
                            scoreAnswerVO.setId(score.getScoreId());
                            QueryWrapper<SubmitContent> queryWrapper = new QueryWrapper<>();
                            queryWrapper
                                    .eq("A.question_id", independentVariableId)
                                    .eq("B.question_id", dependentVariableId)
                                    .eq("A.content", String.valueOf(i + 1))
                                    .eq("B.content", String.valueOf(score.getScoreId()));
                            int count = submitContentMapper.getCountByCrossAnalysis(queryWrapper);
                            scoreAnswerVO.setCount(count);
                            scoreAnswerVO.setContent(score.getScoreContent());
                            if (sum == 0) {
                                scoreAnswerVO.setPercentage("0.0%");
                            } else {
                                scoreAnswerVO.setPercentage(String.format("%.1f", (double) count / sum * 100) + "%");
                            }
                            scoreAnswerVOS.add(scoreAnswerVO);
                        }
                        answerResultVO.setScoreAnswerVOS(scoreAnswerVOS);
                    }
                    answerAnalysisVO.getAnswerResultVOS().add(answerResultVO);
                }
            } else if (question1.getType() == 2) {
                scores = scoreMapper.selectList(new QueryWrapper<Score>().eq("question_id", independentVariableId));
                for (Score score : scores) {
                    QuestionContentVO questionContentVO = new QuestionContentVO();
                    questionContentVO.setId(score.getScoreId());
                    questionContentVO.setContent(score.getScoreContent());
                    answerAnalysisVO.getQuestionContentVOS().add(questionContentVO);
                }
                for (int i = 0; i < scores.size(); i++) {
                    int sum = submitContentMapper.selectCount(new QueryWrapper<SubmitContent>().eq("question_id", independentVariableId).eq("content", String.valueOf(i + 1)));
                    AnswerResultVO answerResultVO = new AnswerResultVO();
                    answerResultVO.setQuestionId(dependentVariableId);
                    answerResultVO.setType(question2.getType());
                    answerResultVO.setSum(sum);
                    if (question2.getType() == 0 || question2.getType() == 1 || question1.getType() == 5 || question1.getType() == 6) {
                        choiceList = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", dependentVariableId));
                        List<ChoiceAnswerVO> choiceAnswerVOS = new ArrayList<>();
                        for (Choice choice : choiceList) {
                            ChoiceAnswerVO choiceAnswerVO = new ChoiceAnswerVO();
                            choiceAnswerVO.setId(choice.getChoiceId());
                            QueryWrapper<SubmitContent> queryWrapper = new QueryWrapper<>();
                            queryWrapper
                                    .eq("A.question_id", independentVariableId)
                                    .eq("B.question_id", dependentVariableId)
                                    .eq("A.content", String.valueOf(i + 1))
                                    .eq("B.content", String.valueOf(choice.getChoiceId()));
                            int count = submitContentMapper.getCountByCrossAnalysis(queryWrapper);
                            choiceAnswerVO.setCount(count);
                            choiceAnswerVO.setContent(choice.getChoiceContent());
                            if (sum == 0) {
                                choiceAnswerVO.setPercentage("0.0%");
                            } else {
                                choiceAnswerVO.setPercentage(String.format("%.1f", (double) count / sum * 100) + "%");
                            }
                            choiceAnswerVOS.add(choiceAnswerVO);
                        }
                        answerResultVO.setChoiceAnswerVOS(choiceAnswerVOS);
                    } else if (question2.getType() == 2) {
                        scoreList = scoreMapper.selectList(new QueryWrapper<Score>().eq("question_id", dependentVariableId));
                        List<ScoreAnswerVO> scoreAnswerVOS = new ArrayList<>();
                        for (Score score : scoreList) {
                            ScoreAnswerVO scoreAnswerVO = new ScoreAnswerVO();
                            scoreAnswerVO.setId(score.getScoreId());
                            QueryWrapper<SubmitContent> queryWrapper = new QueryWrapper<>();
                            queryWrapper
                                    .eq("A.question_id", independentVariableId)
                                    .eq("B.question_id", dependentVariableId)
                                    .eq("A.content", String.valueOf(i + 1))
                                    .eq("B.content", String.valueOf(score.getScoreId()));
                            int count = submitContentMapper.getCountByCrossAnalysis(queryWrapper);
                            scoreAnswerVO.setCount(count);
                            scoreAnswerVO.setContent(score.getScoreContent());
                            if (sum == 0) {
                                scoreAnswerVO.setPercentage("0.0%");
                            } else {
                                scoreAnswerVO.setPercentage(String.format("%.1f", (double) count / sum * 100) + "%");
                            }
                            scoreAnswerVOS.add(scoreAnswerVO);
                        }
                        answerResultVO.setScoreAnswerVOS(scoreAnswerVOS);
                    }
                    answerAnalysisVO.getAnswerResultVOS().add(answerResultVO);
                }
            }
            return ReturnResponse.OK(answerAnalysisVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    public ReturnResponse<ExamResultVO> getExamResult(Integer questionnaireId, String ip, Integer submitRecordId) {
        try {
            ExamResultVO examResultVO = new ExamResultVO();
            Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
            SubmitRecord submitRecord = submitRecordMapper.selectById(submitRecordId);
            examResultVO.setScore(submitRecord.getScore()); // 客观题总分

            // 考试问卷题目初始顺序
            List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("questionnaire_id", questionnaireId));
            // 考试问卷实际答题顺序
            List<Question> trueQuestionList = new ArrayList<>();
            if (questionnaire.getIsOrder() == 0) {
                trueQuestionList.addAll(questionList);
            } else if (questionnaire.getIsOrder() == 1) { // 乱序
                List<Exam> exams = examMapper.selectList(new QueryWrapper<Exam>()
                        .eq("ip", ip).eq("questionnaire_id", questionnaireId));
                for (Exam exam : exams) {
                    trueQuestionList.add(questionMapper.selectById(exam.getQuestionId()));
                }
            }

            Integer isNumber = questionnaire.getIsNumber();
            Double allScore = 0d;
            Double score = 0d;
            Integer correctCount = 0;
            for (int i = 0; i < trueQuestionList.size(); i++) {
                Question question = trueQuestionList.get(i);
                if (question.getScore() != null) {
                    allScore += question.getScore();
                }
                // 返回题目
                QuestionContentVO questionContentVO = new QuestionContentVO();
                questionContentVO.setId(i + 1);
                if (isNumber == 0) {
                    int j = i + 1;
                    questionContentVO.setContent(j + "." + question.getContent());
                } else {
                    questionContentVO.setContent(question.getContent());
                }
                examResultVO.getQuestionContentVOS().add(questionContentVO);

                // 返回其他
                ExamAnswerResultVO examAnswerResultVO = new ExamAnswerResultVO();
                examAnswerResultVO.setQuestionId(question.getId());
                examAnswerResultVO.setType(question.getType());
                if (question.getType() == 4) {
                    examAnswerResultVO.setMax(question.getMaxScore());
                }
                examAnswerResultVO.setScore(question.getScore());
                List<SubmitContent> submitContents = submitContentMapper.selectList(new QueryWrapper<SubmitContent>()
                        .eq("submit_record_id", submitRecordId).eq("question_id", question.getId()));
                examAnswerResultVO.setRealScore(submitContents.get(submitContents.size() - 1).getScore());
                score += examAnswerResultVO.getRealScore();
                if (question.getAnswer() == null) { // 不算分
                    if (question.getType() == 0 || question.getType() == 1) {
                        List<Choice> choices = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", question.getId()));
                        int flag = 0;
                        for (Choice choice : choices) {
                            if (choice.getIsAnswer() == 1) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            examAnswerResultVO.setIsCorrect(0);
                        } else {
                            if (examAnswerResultVO.getScore().equals(examAnswerResultVO.getRealScore())) {
                                examAnswerResultVO.setIsCorrect(1);
                                correctCount++;
                            } else {
                                examAnswerResultVO.setIsCorrect(2);
                            }
                        }
                    } else {
                        examAnswerResultVO.setIsCorrect(0);
                    }
                } else if (examAnswerResultVO.getScore().equals(examAnswerResultVO.getRealScore())) {
                    examAnswerResultVO.setIsCorrect(1);
                    correctCount++;
                } else {
                    examAnswerResultVO.setIsCorrect(2);
                }
                if (question.getType() == 0 || question.getType() == 1) {
                    List<Choice> choices = choiceMapper.selectList(new QueryWrapper<Choice>().eq("question_id", question.getId()));
                    for (int j = 0; j < choices.size(); j++) {
                        if (choices.get(j).getIsAnswer() == 1) {
//                            examAnswerResultVO.getCorrectAnswer().add(j + 1 + "");
                            examAnswerResultVO.getCorrectAnswer().add(choices.get(j).getChoiceContent());
                        }
                    }
                    for (SubmitContent submitContent : submitContents) {
//                        examAnswerResultVO.getTrueAnswer().add(submitContent.getContent());
                        if (submitContent.getContent().equals("")) {
                            examAnswerResultVO.getTrueAnswer().add("");
                        } else {
                            examAnswerResultVO.getTrueAnswer().add(choices.get(Integer.parseInt(submitContent.getContent()) - 1).getChoiceContent());
                        }
                    }
                } else if (question.getType() == 3) {
                    if (question.getAnswer() != null) {
                        examAnswerResultVO.getCorrectAnswer().add(question.getAnswer());
                    }
                    for (SubmitContent submitContent : submitContents) {
                        examAnswerResultVO.getTrueAnswer().add(submitContent.getContent());
                    }
                } else if (question.getType() == 4) {
                    if (question.getAnswer() != null) {
                        String[] split = question.getAnswer().split(";");
                        for (String s : split) {
                            examAnswerResultVO.getCorrectAnswer().add(s);
                        }
                    }
                    for (SubmitContent submitContent : submitContents) {
                        examAnswerResultVO.getTrueAnswer().add(submitContent.getContent());
                    }
                }
                examResultVO.getExamAnswerResultVOS().add(examAnswerResultVO);
            }
            examResultVO.setAllScore(allScore);
            examResultVO.setScore(score);
            examResultVO.setCorrectCount(correctCount);
            return ReturnResponse.OK(examResultVO);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> getQuestionnaireCount(Integer questionnaireId) {
        try {
            Integer count = submitRecordMapper.selectCount(new QueryWrapper<SubmitRecord>().eq("questionnaire_id", questionnaireId));
            return ReturnResponse.OK("" + count);
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }
}
