package whut.yy.service_edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import whut.yy.service_edu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-11-02
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> getPopTeachers();//id正序，查询前4个讲师

    Map<String, Object> pageListFront(Page<EduTeacher> teacherPage);
}
