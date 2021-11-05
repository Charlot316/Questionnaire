package team.buaa.questionnaire.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.entity.domain.Questionnaire;
import team.buaa.questionnaire.entity.dto.*;
import team.buaa.questionnaire.entity.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.List;

public interface IQuestionnaireService extends IService<Questionnaire> {
    ReturnResponse<Page<QuestionnaireInfVO>> getQuestionnaireInf(QuestionnaireInfScreenDTO questionnaireInfScreenDTO);
    ReturnResponse<String> createQuestionnaire(QuestionnaireDTO questionnaireDTO);
    ReturnResponse<String> copyQuestionnaire(ReplicationDTO replicationDTO);
    ReturnResponse<String> starQuestionnaire(StarDTO starDTO);
    ReturnResponse<String> noStarQuestionnaire(StarDTO starDTO);
    ReturnResponse publishQuestionnaire(StarDTO starDTO);
    ReturnResponse<String> stopQuestionnaire(StarDTO starDTO);
    ReturnResponse<String> deleteQuestionnaire(StarDTO starDTO);
    ReturnResponse<Page<QuestionnaireInfVO>> getRecycleQuestionnaireInf(QuestionnaireInfScreenDTO questionnaireInfScreenDTO);
    ReturnResponse<String> recycleDelete(Integer userId);
    ReturnResponse<String> deleteRecycleQuestionnaire(DeleteRecycleDTO deleteRecycleDTO);
    ReturnResponse<String> recoverDelete(DeleteRecycleDTO deleteRecycleDTO);
    ReturnResponse<String>  emptyData(DeleteRecycleDTO deleteRecycleDTO);
    ReturnResponse getQuestionnaireContent(Integer userId, Integer questionnaireId);
    ReturnResponse<String> editQuestionnaireSave(QuestionnaireEditDTO questionnaireEditDTO);
    ReturnResponse<String> editQuestionnaireDelete(QuestionnaireEditDeleteDTO questionnaireEditDeleteDTO);
    ReturnResponse getQuestionnaireContentByCode(CheckDTO checkDTO);
    ReturnResponse getQuestionnaireAnswering(Integer questionnaireId, Integer userId);
    ReturnResponse<String> getAnsweringToExcel(HttpServletResponse response, ExcelExportDTO excelExportDTO);
    ReturnResponse<String> editQuestionnaireCopy(QuestionnaireDTO questionnaireDTO);
    ReturnResponse<String> getCode(Integer questionnaireId);
    ReturnResponse getQuestionnaireAnswerAnalysis(Integer questionnaireId, Integer userId);
    ReturnResponse<CompletionDetailVO> getCompletionAnswer(Integer questionId);
    ReturnResponse<VotesResultVO> getVotesResult(Integer questionnaireId);
    ReturnResponse getExamByCode(CheckExamDTO checkExamDTO);
    ReturnResponse getCrossAnalysis(AnalysisDTO analysisDTO);
    ReturnResponse<ExamResultVO> getExamResult(Integer questionnaireId, String ip, Integer submitRecordId);
    ReturnResponse<String> getQuestionnaireCount(Integer questionnaireId);
}
