package whut.yy.service_edu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import whut.yy.service_edu.client.fallback.UCenterClientFallback;
import whut.yy.service_edu.config.FeignConfiguration;

@FeignClient(name = "service-ucenter", fallback = UCenterClientFallback.class,
        configuration = FeignConfiguration.class)
public interface UCenterClient {

    @GetMapping("/ucenter/member/getUserInfo/{memberId}")
    String getLoginInfo(@PathVariable("memberId") String memberId);
}
