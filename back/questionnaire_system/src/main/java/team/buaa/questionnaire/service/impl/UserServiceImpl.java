package team.buaa.questionnaire.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.common.enums.ReturnStatus;
import team.buaa.questionnaire.common.util.IpUtil;
import team.buaa.questionnaire.entity.domain.User;
import team.buaa.questionnaire.entity.dto.LoginDTO;
import team.buaa.questionnaire.entity.dto.PasswordDTO;
import team.buaa.questionnaire.entity.dto.UserDTO;
import team.buaa.questionnaire.entity.vo.QuestionnaireInfVO;
import team.buaa.questionnaire.entity.vo.UserInfVO;
import team.buaa.questionnaire.mapper.UserMapper;
import team.buaa.questionnaire.service.IUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ReturnResponse<String> login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) {
        try {
            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();
            Integer isAutoLogin = loginDTO.getIsAutoLogin();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("username", username);
            User user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                return ReturnResponse.packageObject("用户名不存在", ReturnStatus.FAILURE);
            } else {
                if (!user.getPassword().equals(password)) {
                    return ReturnResponse.packageObject("密码错误", ReturnStatus.FAILURE);
                } else {
                    request.getSession().removeAttribute("userId");
                    request.getSession().removeAttribute("isAutoLogin");
                    /*WebUtils.setSessionAttribute(request, "userId" + user.getId(), user.getId());
                    WebUtils.setSessionAttribute(request, "isAutoLogin" + user.getId(), isAutoLogin);*/
                    Cookie cookie = new Cookie("userId", user.getId() + "");
                    Cookie cookie1 = new Cookie("isAutoLogin", isAutoLogin + "");
                    cookie.setPath("/");
                    cookie1.setPath("/");
                    response.addCookie(cookie);
                    response.addCookie(cookie1);
                    String ipAddress = IpUtil.getIpAddr(request);
                    System.out.println(ipAddress + "---------------------");
                    return ReturnResponse.OK("登录成功");
                }
            }
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> register(UserDTO userDTO) {
        try {
            String username = userDTO.getUsername();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("username", username);
            if (userMapper.selectOne(queryWrapper) != null) {
                return ReturnResponse.packageObject("用户名已存在", ReturnStatus.FAILURE);
            } else {
                User user = new User();
                user.setUsername(username);
                user.setPassword(userDTO.getPassword());
                user.setSex(userDTO.getSex());
                user.setEmail(userDTO.getEmail());
                user.setPhoneNumber(userDTO.getPhoneNumber());
                userMapper.insert(user);
                return ReturnResponse.OK("注册成功");
            }
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<UserInfVO> information(Integer userId) {
        try{
            QueryWrapper<UserInfVO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", userId);
            UserInfVO userInfVO = userMapper.getUserInformation(queryWrapper);
            return ReturnResponse.OK(userInfVO);
        }catch(Exception e){
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> userInformation(UserDTO userDTO,Integer userId) {
        try{
            User user = new User();
//            user.setId(1);
            user.setId(userId);
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setSex(userDTO.getSex());
            userMapper.updateById(user);
            return ReturnResponse.OK("成功修改个人信息");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }
    }

    @Override
    public ReturnResponse<String> password(Integer userId, PasswordDTO passwordDTO) {
        try{
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            String password = passwordDTO.getPassword();
            userUpdateWrapper.eq("id",userId);
//            userUpdateWrapper.eq("id",1);
            System.out.println(password);
            User user = new User();
            user.setPassword(password);
            userMapper.update(user,userUpdateWrapper);
            return ReturnResponse.OK("成功修改个人密码");
        } catch (Exception e) {
            return ReturnResponse.systemException(ReturnStatus.BUSINESS_EXCEPTION);
        }

    }


}
