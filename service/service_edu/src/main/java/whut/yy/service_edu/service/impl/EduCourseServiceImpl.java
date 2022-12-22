package whut.yy.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_edu.entity.EduCourse;
import whut.yy.service_edu.entity.EduCourseDescription;
import whut.yy.service_edu.entity.frontVo.CourseDetailVO;
import whut.yy.service_edu.entity.frontVo.CourseFrontVO;
import whut.yy.service_edu.entity.vo.CourseInfoVO;
import whut.yy.service_edu.entity.vo.CoursePublishVo;
import whut.yy.service_edu.entity.vo.CourseQuery;
import whut.yy.service_edu.mapper.EduCourseMapper;
import whut.yy.service_edu.service.EduChapterService;
import whut.yy.service_edu.service.EduCourseDescriptionService;
import whut.yy.service_edu.service.EduCourseService;
import whut.yy.service_edu.service.EduVideoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Override
    public String addCourseInfo(CourseInfoVO courseInfoVO) {
        //保存课程基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO, course);
        int insert = baseMapper.insert(course);
        if (insert < 1) {
            throw new MyGlobalException(20001, "课程信息保存失败");
        }

        //保存课程详情信息
        EduCourseDescription courseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVO, courseDescription);
        courseDescription.setId(course.getId());
        boolean resultDescription = courseDescriptionService.save(courseDescription);
        if (!resultDescription) {
            throw new MyGlobalException(20001, "课程详情信息保存失败");
        }
        return course.getId();
    }

    @Override
    public CourseInfoVO getCourseInfo(String courseId) {
        //查询课程信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //查询简介信息
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        //查询的信息封装到VO
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        BeanUtils.copyProperties(eduCourse, courseInfoVO);
        courseInfoVO.setDescription(courseDescription.getDescription());
        return courseInfoVO;
    }

    @Override
    public void updateCourseInfo(CourseInfoVO courseInfoVO) {
        //还原数据库表对应实体类,修改课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0) {
            throw new MyGlobalException(20001, "修改课程信息失败");
        }

        //修改简介信息
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseInfoVO.getId());
        courseDescription.setDescription(courseInfoVO.getDescription());
        boolean update1 = courseDescriptionService.updateById(courseDescription);
        if (!update1) {
            throw new MyGlobalException(20001, "修改课程简介失败");
        }
    }

    @Override
    public CoursePublishVo getCoursePublishInfo(String courseId) {
        //CoursePublishVo 对应 课程发布信息的显示，涉及多张表
        //直接通过sql查询语句查出，再封装到该类中，避免查询多次
        return baseMapper.getCoursePublishInfo(courseId);
    }

    @Override
    public boolean publishCourse(String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus(EduCourse.COURSE_NORMAL);
        int i = baseMapper.updateById(eduCourse);
        return i > 0;
    }

    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");

        if (courseQuery == null) {
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }

        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id", teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id", subjectId);
        }

        baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public boolean removeCourseById(String courseId) {
        //删除小节
        //主键不是courseId，只是其中一个字段，所以需要自己实现service方法进行删除
        videoService.removeVideoByCourseId(courseId);

        //删除章节
        //主键不是courseId，只是其中一个字段，所以需要自己实现service方法进行删除
        chapterService.removeChapterByCourseId(courseId);

        //删除简介
        //因为主键是courseId，所以直接调用封装好的service方法
        courseDescriptionService.removeById(courseId);

        //删除课程
        int i = baseMapper.deleteById(courseId);
        if (i == 0) {
            throw new MyGlobalException(20001, "删除课程失败");
        }
        return true;
    }

    @Cacheable(value = "EduCourse", key = "'getPopCourses'")
    @Override
    public List<EduCourse> getPopCourses() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> pageListFront(Page<EduCourse> teacherPage, CourseFrontVO courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(courseQuery.getSubjectId())) {
            queryWrapper.eq("subject_id", courseQuery.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseQuery.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseQuery.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseQuery.getPriceSort())) {
            queryWrapper.orderByDesc("price");
        }

        baseMapper.selectPage(teacherPage, queryWrapper);

        List<EduCourse> records = teacherPage.getRecords();
        long current = teacherPage.getCurrent();
        long pages = teacherPage.getPages();
        long size = teacherPage.getSize();
        long total = teacherPage.getTotal();
        boolean hasNext = teacherPage.hasNext();
        boolean hasPrevious = teacherPage.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseDetailVO getCourseDetailInfo(String courseId) {
        return baseMapper.getCourseDetailInfo(courseId);
    }
}
