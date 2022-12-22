package whut.yy.service_ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"whut.yy"})
@MapperScan("whut.yy.service_ucenter.mapper")
@EnableFeignClients
public class UCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UCenterApplication.class, args);
    }
}
