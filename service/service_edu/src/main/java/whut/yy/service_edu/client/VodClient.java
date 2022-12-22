package whut.yy.service_edu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import whut.yy.common_util.R;
import whut.yy.service_edu.client.fallback.VodClientFallback;
import whut.yy.service_edu.config.FeignConfiguration;

import java.util.List;

@FeignClient(name = "service-vod", fallback = VodClientFallback.class,
        configuration = FeignConfiguration.class)
public interface VodClient {

    @DeleteMapping("/service_vod/video/{videoId}")
    R deleteVideo(@PathVariable("videoId") String videoId);//@PathVariable注解一定要指定参数名称，否则出错

    @DeleteMapping("/service_vod/video/batchDelete")
    R batchDeleteVideo(@RequestParam List<String> videoIds);
}
