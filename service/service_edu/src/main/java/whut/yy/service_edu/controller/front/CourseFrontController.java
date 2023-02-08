package whut.yy.service_edu.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.JwtUtils;
import whut.yy.common_util.R;
import whut.yy.service_edu.client.OrderClient;
import whut.yy.service_edu.entity.EduCourse;
import whut.yy.service_edu.entity.frontVo.CourseDetailVO;
import whut.yy.service_edu.entity.frontVo.CourseFrontVO;
import whut.yy.service_edu.entity.vo.ChapterVo;
import whut.yy.service_edu.service.EduChapterService;
import whut.yy.service_edu.service.EduCourseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "前台课程相关")
@RestController
@RequestMapping("/service_edu/courseFront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

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
    public R getCourseDetailInfo(@PathVariable String courseId, HttpServletRequest request) {
        //获取课程基本信息(含分类、讲师、具体描述信息)
        CourseDetailVO courseDetailInfo = courseService.getCourseDetailInfo(courseId);

        //获取课程下章节、小节信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoListByCourseId(courseId);

        //查询课程是否已经支付
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean orderHasPay = false;
        //登陆了才查询
        if (!StringUtils.isEmpty(memberId))
            orderHasPay = orderClient.orderHasPay(courseId, memberId);

        return R.ok().data("courseDetailInfo", courseDetailInfo)
                .data("chapterVideoList", chapterVideoList)
                .data("orderHasPay", orderHasPay);
    }

    @ApiOperation(value = "查询单个课程信息(供生成订单远程调用使用)")
    @GetMapping("courseInfo/{courseId}")
    public String getCourseInfoByCourseId(@PathVariable String courseId) {
        //获取课程基本信息(含分类、讲师、具体描述信息)
        CourseDetailVO courseDetailInfo = courseService.getCourseDetailInfo(courseId);
        Gson gson = new Gson();
        return gson.toJson(courseDetailInfo);
    }
}
