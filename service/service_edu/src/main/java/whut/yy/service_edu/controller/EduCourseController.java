package whut.yy.service_edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.R;
import whut.yy.service_edu.entity.EduCourse;
import whut.yy.service_edu.entity.vo.CourseInfoVO;
import whut.yy.service_edu.entity.vo.CoursePublishVo;
import whut.yy.service_edu.entity.vo.CourseQuery;
import whut.yy.service_edu.service.EduCourseService;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
@Api(description = "课程相关")
@RestController
@RequestMapping("/service_edu/course")
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "获取课程列表(分页)")
    @PostMapping("pageList/{page}/{limit}")
    public R getCoursePageInfo(@PathVariable long page,
                               @PathVariable long limit,
                               @RequestBody(required = false) CourseQuery courseQuery) {
        Page<EduCourse> pageParam = new Page<>(page, limit);

        courseService.pageQuery(pageParam, courseQuery);
        List<EduCourse> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "添加课程相关信息")
    @PostMapping
    public R addCourseInfo(@ApiParam(required = true) @RequestBody CourseInfoVO courseInfoVO) {
        String courseId = courseService.addCourseInfo(courseInfoVO);
        return R.ok().data("courseId", courseId);
    }

    @ApiOperation(value = "查询单个课程信息")
    @GetMapping("{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {
        CourseInfoVO courseInfoVO = courseService.getCourseInfo(courseId);
        return R.ok().data("item", courseInfoVO);
    }

    @ApiOperation(value = "修改课程信息")
    @PutMapping
    public R updateCourseInfo(@RequestBody CourseInfoVO courseInfoVO) {
        courseService.updateCourseInfo(courseInfoVO);
        return R.ok();
    }

    @ApiOperation(value = "查询课程发布信息")
    @GetMapping("course-publish/{courseId}")
    public R getCoursePublishInfo(@PathVariable String courseId) {
        CoursePublishVo coursePublishVo = courseService.getCoursePublishInfo(courseId);
        return R.ok().data("item", coursePublishVo);
    }

    @ApiOperation(value = "发布课程")
    @PostMapping("{courseId}")
    public R publishCourse(@PathVariable String courseId) {
        boolean isSuccess = courseService.publishCourse(courseId);
        if (isSuccess)
            return R.ok();
        else return R.error();
    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("{courseId}")
    public R removeCourseById(@PathVariable String courseId) {
        boolean result = courseService.removeCourseById(courseId);
        if (result) {
            return R.ok();
        } else {
            return R.error().message("删除失败");
        }
    }
}

