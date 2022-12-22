package whut.yy.service_edu.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.R;
import whut.yy.service_edu.entity.EduCourse;
import whut.yy.service_edu.entity.EduTeacher;
import whut.yy.service_edu.service.EduCourseService;
import whut.yy.service_edu.service.EduTeacherService;

import java.util.List;
import java.util.Map;

@Api(description = "前台讲师相关")
@RestController
@RequestMapping("/service_edu/teacherFront")
@CrossOrigin
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "获取讲师分页数据")
    @GetMapping("pageInfo/{page}/{limit}")
    public R getPageInfo(@PathVariable long page, @PathVariable long limit) {
        Page<EduTeacher> teacherPage = new Page<>(page, limit);
        Map<String, Object> map = teacherService.pageListFront(teacherPage);
        return R.ok().data(map);
    }

    @ApiOperation(value = "获取讲师详情数据")
    @GetMapping("teacherInfo/{teacherId}")
    public R getTeacherInfo(@PathVariable String teacherId) {
        //查询讲师信息
        EduTeacher teacher = teacherService.getById(teacherId);

        //查询讲师所属课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);

        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }
}
