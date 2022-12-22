package whut.yy.service_cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"whut.yy"}) //各个模块包名为此的都作为组件扫描交给容器管理
@MapperScan("whut.yy.service_cms.mapper") //mapper没有实现，所有在这里配置扫描包
public class CmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class, args);
    }
}
