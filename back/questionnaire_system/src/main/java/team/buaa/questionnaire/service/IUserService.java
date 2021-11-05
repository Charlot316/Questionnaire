package team.buaa.questionnaire.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.entity.domain.User;
import team.buaa.questionnaire.entity.dto.LoginDTO;
import team.buaa.questionnaire.entity.dto.PasswordDTO;
import team.buaa.questionnaire.entity.dto.UserDTO;
import team.buaa.questionnaire.entity.vo.UserInfVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IUserService extends IService<User> {
    ReturnResponse<String> login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO);
    ReturnResponse<String> register(UserDTO userDTO);
    ReturnResponse<UserInfVO> information(Integer userId);
    ReturnResponse<String> userInformation(UserDTO userDTO,Integer userId);
    ReturnResponse<String> password(Integer userId, PasswordDTO passwordDTO);
}
