package team.buaa.questionnaire.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import team.buaa.questionnaire.entity.domain.SubmitRecord;

@Mapper
public interface SubmitRecordMapper extends BaseMapper<SubmitRecord> {
}
