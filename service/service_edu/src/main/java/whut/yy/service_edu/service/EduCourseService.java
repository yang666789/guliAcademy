package whut.yy.service_edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import whut.yy.service_edu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import whut.yy.service_edu.entity.frontVo.CourseDetailVO;
import whut.yy.service_edu.entity.frontVo.CourseFrontVO;
import whut.yy.service_edu.entity.vo.CourseInfoVO;
import whut.yy.service_edu.entity.vo.CoursePublishVo;
import whut.yy.service_edu.entity.vo.CourseQuery;

import java.util.List;
import java.util.Map;

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

    List<EduCourse> getPopCourses();//id倒序，查询前8个课程

    //前台分页查询课程信息（筛选条件可选）
    Map<String, Object> pageListFront(Page<EduCourse> teacherPage, CourseFrontVO courseFrontVO);

    CourseDetailVO getCourseDetailInfo(String courseId);
}
