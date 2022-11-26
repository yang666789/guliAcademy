package whut.yy.service_edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import whut.yy.service_edu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import whut.yy.service_edu.entity.vo.CourseInfoVO;
import whut.yy.service_edu.entity.vo.CoursePublishVo;
import whut.yy.service_edu.entity.vo.CourseQuery;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
public interface EduCourseService extends IService<EduCourse> {

    String addCourseInfo(CourseInfoVO courseInfoVO);

    CourseInfoVO getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVO courseInfoVO);

    CoursePublishVo getCoursePublishInfo(String courseId);

    boolean publishCourse(String courseId);

    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    boolean removeCourseById(String courseId);
}
