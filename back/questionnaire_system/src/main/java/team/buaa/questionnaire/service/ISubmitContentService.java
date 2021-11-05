package team.buaa.questionnaire.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.entity.domain.SubmitContent;
import team.buaa.questionnaire.entity.dto.SubmitContentDTO;

import javax.servlet.http.HttpServletRequest;

public interface ISubmitContentService extends IService<SubmitContent> {
    ReturnResponse<String> submitQuestionnaire(SubmitContentDTO submitContentDTO);
    ReturnResponse<String> submitExam(SubmitContentDTO submitContentDTO);
}
