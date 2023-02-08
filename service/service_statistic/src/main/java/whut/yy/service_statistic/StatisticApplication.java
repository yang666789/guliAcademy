package whut.yy.service_statistic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableDiscoveryClient //此版本不需要加上该注解
@EnableFeignClients //feign远程调用
@SpringBootApplication
@ComponentScan(basePackages = {"whut.yy"}) //各个模块包名为此的都作为组件扫描交给容器管理
@MapperScan("whut.yy.service_statistic.mapper") //mapper没有实现，所有在这里配置扫描包
@EnableScheduling //启动定时任务
public class StatisticApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticApplication.class, args);
    }
}
