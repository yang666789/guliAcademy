package whut.yy.service_vod.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whut.yy.common_util.R;
import whut.yy.service_vod.service.VideoService;

import java.util.List;

@Api(description = "阿里云视频点播服务")
@RestController
@RequestMapping("/service_vod/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "上传视频")
    @PostMapping("upload")
    public R uploadVideo(@ApiParam(name = "file", value = "文件", required = true)
                         @RequestParam("file") MultipartFile file) {
        String videoId = videoService.uploadVideo(file);
        return R.ok().message("视频上传成功").data("videoId", videoId);
    }

    @ApiOperation(value = "删除视频")
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId) {
        videoService.deleteVideo(videoId);
        return R.ok();
    }

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("batchDelete")
    public R batchDeleteVideo(@RequestParam List<String> videoIds) {
        videoService.batchDeleteVideo(videoIds);
        return R.ok();
    }

    @ApiOperation(value = "获取阿里云视频播放凭证")
    @GetMapping("getPlayAuth/{videoSourceId}")
    public R getPlayAuth(@PathVariable String videoSourceId) {
        String playAuth = videoService.getPlayAuth(videoSourceId);
        return R.ok().data("playAuth", playAuth);
    }
}
