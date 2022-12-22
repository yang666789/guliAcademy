package whut.yy.service_edu.controller.front;

import ch.qos.logback.core.pattern.ConverterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whut.yy.common_util.R;
import whut.yy.service_edu.entity.EduCourse;
import whut.yy.service_edu.entity.EduTeacher;
import whut.yy.service_edu.service.EduCourseService;
import whut.yy.service_edu.service.EduTeacherService;

import java.util.List;

@Api(description = "前台首页相关")
@RestController
@RequestMapping("/service_edu/index")
@CrossOrigin
public class IndexController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "获取前台首页课程、讲师数据")
    @GetMapping("courseTeachers")
    public R index() {
        //id倒序，查询前8个课程
        List<EduCourse> courseList = courseService.getPopCourses();
        //id正序，查询前4个讲师
        List<EduTeacher> teacherList = teacherService.getPopTeachers();
        //返回结果
        return R.ok().data("courseList", courseList).data("teacherList", teacherList);
    }
}
