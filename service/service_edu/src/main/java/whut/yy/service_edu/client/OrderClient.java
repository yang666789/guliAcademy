package whut.yy.service_edu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import whut.yy.service_edu.client.fallback.OrderClientFallback;
import whut.yy.service_edu.config.FeignConfiguration;

@FeignClient(name = "service-order", fallback = OrderClientFallback.class,
        configuration = FeignConfiguration.class)
public interface OrderClient {

    //@PathVariable注解一定要指定参数名称，否则出错
    @GetMapping("/service_order/order/orderHasPay/{courseId}/{memberId}")
    boolean orderHasPay(@PathVariable("courseId") String courseId,
                        @PathVariable("memberId") String memberId);
}
