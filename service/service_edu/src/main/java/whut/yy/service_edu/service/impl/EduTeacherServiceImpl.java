package whut.yy.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import whut.yy.service_edu.entity.EduTeacher;
import whut.yy.service_edu.mapper.EduTeacherMapper;
import whut.yy.service_edu.service.EduTeacherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-11-02
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Cacheable(value = "EduTeacher", key = "'getPopTeachers'")
    @Override
    public List<EduTeacher> getPopTeachers() {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        wrapper.last("limit 4");
        return baseMapper.selectList(wrapper);
    }

    /**
     * 前台自定义分页，需要map参数
     *
     * @param teacherPage
     * @return
     */
    @Override
    public Map<String, Object> pageListFront(Page<EduTeacher> teacherPage) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByDesc("id");

        baseMapper.selectPage(teacherPage, queryWrapper);

        List<EduTeacher> records = teacherPage.getRecords();
        long current = teacherPage.getCurrent();
        long pages = teacherPage.getPages();
        long size = teacherPage.getSize();
        long total = teacherPage.getTotal();
        boolean hasNext = teacherPage.hasNext();
        boolean hasPrevious = teacherPage.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }
}
