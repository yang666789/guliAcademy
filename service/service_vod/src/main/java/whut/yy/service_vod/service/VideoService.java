package whut.yy.service_vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    String uploadVideo(MultipartFile file);

    void deleteVideo(String videoId);

    void batchDeleteVideo(List<String> videoIds);

    String getPlayAuth(String videoSourceId);
}
