package whut.yy.service_statistic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import whut.yy.service_statistic.client.fallback.UCenterMemberClientFallback;
import whut.yy.service_statistic.config.FeignConfiguration;

@FeignClient(name = "service-ucenter",
        fallback = UCenterMemberClientFallback.class, configuration = FeignConfiguration.class)
public interface UCenterMemberClient {

    @GetMapping("/ucenter/member/getUserRegisterNum/{date}")
    int getUserRegisterNum(@PathVariable("date") String date);
}
