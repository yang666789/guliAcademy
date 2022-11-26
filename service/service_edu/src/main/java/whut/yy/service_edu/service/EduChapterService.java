package whut.yy.service_edu.service;

import whut.yy.service_edu.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import whut.yy.service_edu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoListByCourseId(String courseId);

    boolean removeChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
