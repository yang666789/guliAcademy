package whut.yy.service_edu.client.fallback;

import whut.yy.common_util.R;
import whut.yy.service_edu.client.VodClient;

import java.util.List;

public class VodClientFallback implements VodClient {
    @Override
    public R deleteVideo(String videoId) {
        return R.error().message("删除视频失败");
    }

    @Override
    public R batchDeleteVideo(List<String> videoIds) {
        return R.error().message("批量删除视频失败");
    }
}
