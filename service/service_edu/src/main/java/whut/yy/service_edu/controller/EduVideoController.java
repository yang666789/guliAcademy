package whut.yy.service_edu.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import whut.yy.common_util.R;
import whut.yy.service_edu.entity.EduChapter;
import whut.yy.service_edu.entity.EduVideo;
import whut.yy.service_edu.service.EduVideoService;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
@Api(description = "小节信息")
@RestController
@RequestMapping("/service_edu/video")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @ApiOperation(value = "增加小节")
    @PostMapping
    public R addVideoInfo(@RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "查询小节信息")
    @GetMapping("{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {
        EduVideo eduVideo = videoService.getById(videoId);
        return R.ok().data("item", eduVideo);
    }

    @ApiOperation(value = "修改小节信息")
    @PutMapping
    public R updateVideoInfo(@RequestBody EduVideo eduVideo) {
        videoService.updateById(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "删除小节信息")
    @DeleteMapping("{id}")
    public R deleteVideoById(@PathVariable String id) {
        boolean isSuccess = videoService.removeVideoById(id);
        if (isSuccess)
            return R.ok();
        else return R.error();
    }
}

