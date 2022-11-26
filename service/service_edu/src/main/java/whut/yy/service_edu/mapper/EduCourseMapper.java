package whut.yy.service_edu.mapper;

import whut.yy.service_edu.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import whut.yy.service_edu.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getCoursePublishInfo(String courseId);
}
