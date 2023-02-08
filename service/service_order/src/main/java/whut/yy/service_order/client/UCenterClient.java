package whut.yy.service_order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-ucenter")
public interface UCenterClient {

    @GetMapping("/ucenter/member/getUserInfo/{memberId}")
    String getLoginInfo(@PathVariable("memberId") String memberId);
}
