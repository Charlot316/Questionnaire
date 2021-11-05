package team.buaa.questionnaire.controller.questionnaire;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.buaa.questionnaire.common.Normal;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.common.server.WebSocketServer;
import team.buaa.questionnaire.common.util.IpUtil;
import team.buaa.questionnaire.entity.dto.*;
import team.buaa.questionnaire.entity.vo.*;
import team.buaa.questionnaire.service.IQuestionnaireService;
import team.buaa.questionnaire.service.ISubmitContentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("api/admin")
public class QuestionnaireController {

    @Autowired
    private IQuestionnaireService iQuestionnaireService;
    @Autowired
    private ISubmitContentService iSubmitContentService;

    @ApiOperation("获取问卷基础信息")
    @GetMapping("information")
    public ReturnResponse<Page<QuestionnaireInfVO>> getQuestionnaireInf(HttpServletRequest request, @RequestParam(required = false) Integer type,
                                                                        @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status,
                                                                        @RequestParam(required = false) Integer isStar, @RequestParam(required = false) Integer current,
                                                                        @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer questionnaireType) {
        QuestionnaireInfScreenDTO questionnaireInfScreenDTO = new QuestionnaireInfScreenDTO();
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
//        Integer userId = 1;
        questionnaireInfScreenDTO.setUserId(userId);
        questionnaireInfScreenDTO.setType(type);
        questionnaireInfScreenDTO.setKeyword(keyword);
        questionnaireInfScreenDTO.setStatus(status);
        questionnaireInfScreenDTO.setIsStar(isStar);
        questionnaireInfScreenDTO.setCurrent(current);
        questionnaireInfScreenDTO.setSize(size);
        questionnaireInfScreenDTO.setQuestionnaireType(questionnaireType);
        return iQuestionnaireService.getQuestionnaireInf(questionnaireInfScreenDTO);
    }

    @ApiOperation("创建问卷")
    @PostMapping("questionnaire")
    public ReturnResponse<String> createQuestionnaire(HttpServletRequest request, @RequestBody QuestionnaireDTO questionnaireDTO) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
//        Integer userId = 11;
        questionnaireDTO.setUserId(userId);
        return iQuestionnaireService.createQuestionnaire(questionnaireDTO);
    }

    @ApiOperation("复制问卷")
    @PostMapping("replication")
    public ReturnResponse<String> copyQuestionnaire(@RequestBody ReplicationDTO replicationDTO) {
        return iQuestionnaireService.copyQuestionnaire(replicationDTO);
    }

    @ApiOperation("收藏问卷")
    @PatchMapping("star")
    public ReturnResponse<String> starQuestionnaire(@RequestBody StarDTO starDTO) {
        return iQuestionnaireService.starQuestionnaire(starDTO);
    }

    @ApiOperation("取消收藏问卷")
    @PatchMapping("noStar")
    public ReturnResponse<String> noStarQuestionnaire(@RequestBody StarDTO starDTO) {
        return iQuestionnaireService.noStarQuestionnaire(starDTO);
    }

    @ApiOperation("发布问卷")
    @PatchMapping("publish")
    public ReturnResponse publishQuestionnaire(@RequestBody StarDTO starDTO) {
        return iQuestionnaireService.publishQuestionnaire(starDTO);
    }

    @ApiOperation("暂停问卷")
    @PatchMapping("stop")
    public ReturnResponse<String> stopQuestionnaire(@RequestBody StarDTO starDTO) {
        return iQuestionnaireService.stopQuestionnaire(starDTO);
    }

    @ApiOperation("删除问卷")
    @PatchMapping("delete")
    public ReturnResponse<String> deleteQuestionnaire(@RequestBody StarDTO starDTO) {
        return iQuestionnaireService.deleteQuestionnaire(starDTO);
    }

    @ApiOperation("用户填写问卷")
    @PostMapping("questionnaireContent")
    public ReturnResponse<String> submitQuestionnaire(HttpServletRequest request, @RequestBody SubmitContentDTO submitContentDTO) {
        int userId;
        if (Normal.getUserIdByCookie(request) == null) {
            userId = 0;
        } else {
            userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        }
        submitContentDTO.setUserId(userId);
        return iSubmitContentService.submitQuestionnaire(submitContentDTO);
    }

    @ApiOperation("获取问卷详细内容")
    @GetMapping("questionnaireContent")
    public ReturnResponse getQuestionnaireContent(HttpServletRequest request, @RequestParam Integer questionnaireId) {
        Integer userId;
        if (Normal.getUserIdByCookie(request) == null) {
            userId = 0;
        } else {
            userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        }
        return iQuestionnaireService.getQuestionnaireContent(userId, questionnaireId);
    }

    @ApiOperation("获取问卷详细内容real")
    @PostMapping("questionnaire/answering")
    public ReturnResponse getQuestionnaireContentByCode(HttpServletRequest request, @RequestBody CheckIpDTO checkIpDTO) {
        CheckDTO checkDTO = new CheckDTO();
        if (Normal.getUserIdByCookie(request) == null) { // 未登录
            checkDTO.setIsLogin(0);
        } else { // 已登录
            checkDTO.setIsLogin(1);
            checkDTO.setUserId(Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request))));
        }
        checkDTO.setQuestionnaireId(checkIpDTO.getQuestionnaireId());
        checkDTO.setCode(checkIpDTO.getCode());
        checkDTO.setIp(checkIpDTO.getIp());
        return iQuestionnaireService.getQuestionnaireContentByCode(checkDTO);
    }

    @ApiOperation("编辑问卷保留答卷")
    @PostMapping("editQuestionnaireSave")
    public ReturnResponse<String> editQuestionnaire(HttpServletRequest request, @RequestBody QuestionnaireEditDTO questionnaireEditDTO) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        questionnaireEditDTO.setUserId(userId);
        return iQuestionnaireService.editQuestionnaireSave(questionnaireEditDTO);
    }

    @ApiOperation("编辑问卷删除答卷")
    @PostMapping("editQuestionnaireDelete")
    public ReturnResponse<String> editQuestionnaireDelete(HttpServletRequest request, @RequestBody QuestionnaireEditDeleteDTO questionnaireEditDeleteDTO) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        questionnaireEditDeleteDTO.setUserId(userId);
        return iQuestionnaireService.editQuestionnaireDelete(questionnaireEditDeleteDTO);
    }

    @ApiOperation("获取问卷答题情况")
    @GetMapping("answering")
    public ReturnResponse getQuestionnaireAnswering(HttpServletRequest request, @RequestParam Integer questionnaireId) {
        int userId;
        if (Normal.getUserIdByCookie(request) == null) {
            userId = 0;
        } else {
            userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        }
        return iQuestionnaireService.getQuestionnaireAnswering(questionnaireId, userId);
    }

    @ApiOperation("获取回收站问卷信息")
    @GetMapping("recycleInformation")
    public ReturnResponse<Page<QuestionnaireInfVO>> getRecycleQuestionnaireInf(HttpServletRequest request, @RequestParam(required = false) Integer type,
                                                                               @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status,
                                                                               @RequestParam(required = false) Integer isStar, @RequestParam(required = false) Integer current,
                                                                               @RequestParam(required = false) Integer size) {
        QuestionnaireInfScreenDTO questionnaireInfScreenDTO = new QuestionnaireInfScreenDTO();
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
//        Integer userId = 1;
        questionnaireInfScreenDTO.setUserId(userId);
        questionnaireInfScreenDTO.setType(type);
        questionnaireInfScreenDTO.setKeyword(keyword);
        questionnaireInfScreenDTO.setStatus(status);
        questionnaireInfScreenDTO.setIsStar(isStar);
        questionnaireInfScreenDTO.setCurrent(current);
        questionnaireInfScreenDTO.setSize(size);
        return iQuestionnaireService.getRecycleQuestionnaireInf(questionnaireInfScreenDTO);
    }

    @ApiOperation("清空回收站")
    @PostMapping("recycleDelete")
    public ReturnResponse<String> deleteRecycle(HttpServletRequest request){
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        return iQuestionnaireService.recycleDelete(userId);
    }

    @ApiOperation("删除回收站中的某一问卷")
    @PostMapping("recycleQuestionnaireDelete")
    public ReturnResponse<String> deleteRecycleQuestionnaire(@RequestBody DeleteRecycleDTO deleteRecycleDTO){
        return iQuestionnaireService.deleteRecycleQuestionnaire(deleteRecycleDTO);
    }

    @ApiOperation("恢复回收站中的某一问卷")
    @PostMapping("deleteRecovery")
    public ReturnResponse<String> recoverDelete(@RequestBody DeleteRecycleDTO deleteRecycleDTO){
        return iQuestionnaireService.recoverDelete(deleteRecycleDTO);
    }

    @ApiOperation("清空问卷数据")
    @PostMapping("deleteEmpty")
    public ReturnResponse<String> emptyDelete(@RequestBody DeleteRecycleDTO deleteRecycleDTO){
        return iQuestionnaireService.emptyData(deleteRecycleDTO);
    }

    @ApiOperation("复制问卷编辑")
    @PostMapping("editQuestionnaireCopy")
    public ReturnResponse<String> editQuestionnaireCopy(HttpServletRequest request, @RequestBody QuestionnaireDTO questionnaireDTO) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        questionnaireDTO.setUserId(userId);
        return iQuestionnaireService.editQuestionnaireCopy(questionnaireDTO);
    }

    @ApiOperation("将问卷答题情况导出至excel")
    @PostMapping("excel")
    public ReturnResponse<String> getAnsweringToExcel(HttpServletResponse response, @RequestBody ExcelExportDTO excelExportDTO) {
        return iQuestionnaireService.getAnsweringToExcel(response, excelExportDTO);
    }

    @ApiOperation("获取当前code")
    @GetMapping("code")
    public ReturnResponse<String> getCode(@RequestParam Integer questionnaireId) {
        return iQuestionnaireService.getCode(questionnaireId);
    }

    @ApiOperation("答题情况分析")
    @GetMapping("answerAnalysis")
    public ReturnResponse getQuestionnaireAnswerAnalysis(HttpServletRequest request, @RequestParam Integer questionnaireId) {
        int userId;
        if (Normal.getUserIdByCookie(request) == null) {
            userId = 0;
        } else {
            userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        }
        return iQuestionnaireService.getQuestionnaireAnswerAnalysis(questionnaireId, userId);
    }

    @ApiOperation("填空题详细作答信息")
    @GetMapping("completion")
    public ReturnResponse<CompletionDetailVO> getCompletionAnswer(@RequestParam Integer questionId) {
        return iQuestionnaireService.getCompletionAnswer(questionId);
    }

    @ApiOperation("获取投票问卷结果")
    @GetMapping("votes")
    public ReturnResponse<VotesResultVO> getVotesResult(@RequestParam Integer questionnaireId) {
        return iQuestionnaireService.getVotesResult(questionnaireId);
    }

    @ApiOperation("获取考试问卷")
    @GetMapping("exam")
    public ReturnResponse getExamByCode(@RequestParam String ip, @RequestParam Integer questionnaireId) {
        CheckExamDTO checkExamDTO = new CheckExamDTO();
        checkExamDTO.setIp(ip);
        checkExamDTO.setQuestionnaireId(questionnaireId);
        return iQuestionnaireService.getExamByCode(checkExamDTO);
    }

    @ApiOperation("交叉分析")
    @GetMapping("crossAnalysis")
    public ReturnResponse getCrossAnalysis(HttpServletRequest request, @RequestParam Integer independentVariableId, @RequestParam Integer dependentVariableId) {
        int userId;
        if (Normal.getUserIdByCookie(request) == null) {
            userId = 0;
        } else {
            userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        }
        AnalysisDTO analysisDTO = new AnalysisDTO();
        analysisDTO.setIndependentVariableId(independentVariableId);
        analysisDTO.setDependentVariableId(dependentVariableId);
        analysisDTO.setUserId(userId);
        return iQuestionnaireService.getCrossAnalysis(analysisDTO);
    }

    @ApiOperation("获取考试分析")
    @GetMapping("examAnalysis")
    public ReturnResponse<ExamResultVO> getExamResult(@RequestParam Integer questionnaireId, @RequestParam String ip, @RequestParam Integer submitRecordId) {
        return iQuestionnaireService.getExamResult(questionnaireId, ip, submitRecordId);
    }

    @ApiOperation("获取当前填写问卷数量")
    @GetMapping("count")
    public ReturnResponse<String> getQuestionnaireCount(@RequestParam Integer questionnaireId) {
        return iQuestionnaireService.getQuestionnaireCount(questionnaireId);
    }
}
