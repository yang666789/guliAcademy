package whut.yy.service_edu.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import whut.yy.common_util.R;
import whut.yy.service_edu.entity.EduChapter;
import whut.yy.service_edu.entity.vo.ChapterVo;
import whut.yy.service_edu.service.EduChapterService;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
@Api(description = "章节信息")
@RestController
@RequestMapping("/service_edu/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    @ApiOperation(value = "获取指定课程下的章节和小节信息")
    @GetMapping("chapterVideoList/{courseId}")
    public R getChapterVideoListByCourseId(@PathVariable String courseId) {
        List<ChapterVo> chapterVos = chapterService.getChapterVideoListByCourseId(courseId);
        return R.ok().data("list", chapterVos);
    }

    @ApiOperation(value = "增加章节")
    @PostMapping
    public R addChapterInfo(@RequestBody EduChapter eduChapter) {
        chapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "查询章节信息")
    @GetMapping("{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId) {
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("item", eduChapter);
    }

    @ApiOperation(value = "修改章节信息")
    @PutMapping
    public R updateChapterInfo(@RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "删除章节信息(章节下有小节不能删除)")
    @DeleteMapping("{chapterId}")
    public R deleteChapterById(@PathVariable String chapterId) {
        boolean isSuccess = chapterService.removeChapter(chapterId);
        if (isSuccess)
            return R.ok();
        else return R.error();
    }
}

