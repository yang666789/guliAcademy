package whut.yy.service_edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//@EnableDiscoveryClient //此版本不需要加上该注解
@EnableFeignClients //feign远程调用
@SpringBootApplication
@ComponentScan(basePackages = {"whut.yy"}) //各个模块包名为此的都作为组件扫描交给容器管理
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
