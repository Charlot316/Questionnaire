package team.buaa.questionnaire.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import team.buaa.questionnaire.common.Normal;
import team.buaa.questionnaire.common.config.ReturnResponse;
import team.buaa.questionnaire.common.enums.ReturnStatus;
import team.buaa.questionnaire.entity.dto.LoginDTO;
import team.buaa.questionnaire.entity.dto.PasswordDTO;
import team.buaa.questionnaire.entity.dto.UserDTO;
import team.buaa.questionnaire.entity.vo.UserInfVO;
import team.buaa.questionnaire.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @ApiOperation("自动登录")
    @PostMapping("loginTest")
    public ReturnResponse testLogin(HttpServletRequest request) {
        /*Integer userId = (Integer) WebUtils.getSessionAttribute(request, "userId");
        Integer isAutoLogin = (Integer) WebUtils.getSessionAttribute(request, "isAutoLogin");*/
        if (Normal.getUserIdByCookie(request) != null && Normal.getIsAutoLoginByCookie(request) != null) {
            Integer isAutoLogin = Integer.parseInt(Objects.requireNonNull(Normal.getIsAutoLoginByCookie(request)));
            if (isAutoLogin == 1) {
                Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
                return iUserService.information(userId);
            }
        }
        return ReturnResponse.packageObject("未保存有效账号信息", ReturnStatus.FAILURE);
    }

    @ApiOperation("用户登录")
    @PostMapping("login")
    public ReturnResponse<String> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDTO loginDTO) {
        return iUserService.login(request, response, loginDTO);
    }

    @ApiOperation("用户登出")
    @PostMapping("logout")
    public ReturnResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("userId");
        request.getSession().removeAttribute("isAutoLogin");
        return ReturnResponse.OK("退出登录成功");
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public ReturnResponse<String> register(@RequestBody UserDTO userDTO) {
        return iUserService.register(userDTO);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("information")
    public ReturnResponse<UserInfVO> information(HttpServletRequest request){
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        return iUserService.information(userId);
    }

    @ApiOperation("修改个人信息")
    @PostMapping("userInformation")
    public ReturnResponse<String> userInformation(HttpServletRequest request,@RequestBody UserDTO userDTO){
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        return iUserService.userInformation(userDTO,userId);
    }

    @ApiOperation("修改个人密码")
    @PostMapping("password")
    public ReturnResponse<String> password(HttpServletRequest request, @RequestBody PasswordDTO passwordDTO){
        Integer userId = Integer.parseInt(Objects.requireNonNull(Normal.getUserIdByCookie(request)));
        return iUserService.password(userId,passwordDTO);
    }
}
