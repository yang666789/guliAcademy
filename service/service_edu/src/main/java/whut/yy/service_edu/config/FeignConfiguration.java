package whut.yy.service_edu.config;

import org.springframework.context.annotation.Bean;
import whut.yy.service_edu.client.fallback.UCenterClientFallback;
import whut.yy.service_edu.client.fallback.VodClientFallback;

public class FeignConfiguration {
    @Bean
    public VodClientFallback vodClientFallback() {
        return new VodClientFallback();
    }

    @Bean
    public UCenterClientFallback uCenterClientFallback() {
        return new UCenterClientFallback();
    }
}
