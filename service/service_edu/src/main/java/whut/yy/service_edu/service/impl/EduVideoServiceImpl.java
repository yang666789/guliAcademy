package whut.yy.service_edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import whut.yy.service_edu.client.VodClient;
import whut.yy.service_edu.entity.EduVideo;
import whut.yy.service_edu.mapper.EduVideoMapper;
import whut.yy.service_edu.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2022-11-21
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    // 根据课程id删除小节以及小节下的视频
    @Override
    public void removeVideoByCourseId(String courseId) {
        //查询课程下所有小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.select("video_source_id");
        wrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);
        //组成{1,2,3}
        List<String> videoIds = eduVideos.stream().map(EduVideo::getVideoSourceId)
                .filter(videoSourceId -> !StringUtils.isEmpty(videoSourceId))
                .collect(Collectors.toList());
        //远程调用批量删除阿里云视频
        if (videoIds.size() > 0) {
            vodClient.batchDeleteVideo(videoIds);
        }

        //删除小节
        QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }

    //根据主键id删除小节以及小节下的视频
    @Override
    public boolean removeVideoById(String id) {
        //查询小节
        EduVideo eduVideo = baseMapper.selectById(id);
        //删除小节中的视频
        if (!StringUtils.isEmpty(eduVideo.getVideoSourceId()))
            vodClient.deleteVideo(eduVideo.getVideoSourceId());
        //删除小节
        int i = baseMapper.deleteById(id);
        return i > 0;
    }
}
