package whut.yy.service_vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_vod.service.VideoService;
import whut.yy.service_vod.util.AliyunVodSDKUtils;
import whut.yy.service_vod.util.ConstantPropertiesUtil;

import java.io.InputStream;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            String videoId = "";

            String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
            String fileName = file.getOriginalFilename();//文件名
            String title = fileName.substring(0, fileName.lastIndexOf("."));//标题
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret,
                    title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyGlobalException(20001, "阿里云vod 服务上传失败");
        }

    }

    @Override
    public void deleteVideo(String videoId) {
        try {
            //初始化client
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //构建删除request
            DeleteVideoRequest deleteVideoRequest = new DeleteVideoRequest();
            //设置删除的视频id
            deleteVideoRequest.setVideoIds(videoId);
            //删除
            client.getAcsResponse(deleteVideoRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyGlobalException(20001, "删除阿里云视频出错");
        }
    }

    @Override
    public void batchDeleteVideo(List<String> videoIds) {
        try {
            //初始化client
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //构建删除request
            DeleteVideoRequest deleteVideoRequest = new DeleteVideoRequest();

            //构造"1,2,3"
            String videoIdsStr = StringUtils.join(videoIds, ',');
            //设置删除的视频ids(多个id："1,2,3")
            deleteVideoRequest.setVideoIds(videoIdsStr);
            //删除
            client.getAcsResponse(deleteVideoRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyGlobalException(20001, "批量删除阿里云视频出错");
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) {
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoSourceId);
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            return response.getPlayAuth();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyGlobalException(20001, "获取视频播放凭证失败");
        }
    }
}
