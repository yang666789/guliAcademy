package whut.yy.service_edu.service;

import org.springframework.web.multipart.MultipartFile;
import whut.yy.service_edu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author gagayang
 * @since 2022-11-20
 */
public interface EduSubjectService extends IService<EduSubject> {

    void addSubject(MultipartFile file);
}
