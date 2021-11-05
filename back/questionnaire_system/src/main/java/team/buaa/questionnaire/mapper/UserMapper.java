package team.buaa.questionnaire.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.buaa.questionnaire.entity.domain.User;
import team.buaa.questionnaire.entity.dto.UserDTO;
import team.buaa.questionnaire.entity.vo.QuestionnaireInfVO;
import team.buaa.questionnaire.entity.vo.UserInfVO;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select username, password, sex, email ,phone_number as phoneNumber from user ${ew.customSqlSegment}")
    UserInfVO getUserInformation(@Param(Constants.WRAPPER) QueryWrapper<UserInfVO> queryWrapper);
}
