package whut.yy.service_edu.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.R;
import whut.yy.service_edu.entity.EduCourse;
import whut.yy.service_edu.entity.frontVo.CourseDetailVO;
import whut.yy.service_edu.entity.frontVo.CourseFrontVO;
import whut.yy.service_edu.entity.vo.ChapterVo;
import whut.yy.service_edu.service.EduChapterService;
import whut.yy.service_edu.service.EduCourseService;

import java.util.List;
import java.util.Map;

@Api(description = "前台课程相关")
@RestController
@RequestMapping("/service_edu/courseFront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @ApiOperation(value = "获取课程分页数据")
    @PostMapping("pageInfo/{page}/{limit}")
    public R getPageInfo(@PathVariable long page, @PathVariable long limit,
                         @RequestBody(required = false) CourseFrontVO courseFrontVO) {
        Page<EduCourse> teacherPage = new Page<>(page, limit);
        Map<String, Object> map = courseService.pageListFront(teacherPage, courseFrontVO);
        return R.ok().data(map);
    }

    @ApiOperation(value = "获取课程信息")
    @GetMapping("{courseId}")
    public R getCourseDetailInfo(@PathVariable String courseId) {
        //获取课程基本信息(含分类、讲师、具体描述信息)
        CourseDetailVO courseDetailInfo = courseService.getCourseDetailInfo(courseId);

        //获取课程下章节、小节信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoListByCourseId(courseId);

        return R.ok().data("courseDetailInfo", courseDetailInfo)
                .data("chapterVideoList", chapterVideoList);
    }
}
