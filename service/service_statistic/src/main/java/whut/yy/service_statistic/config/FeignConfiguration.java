package whut.yy.service_statistic.config;

import org.springframework.context.annotation.Bean;
import whut.yy.service_statistic.client.fallback.UCenterMemberClientFallback;

public class FeignConfiguration {
    @Bean
    public UCenterMemberClientFallback uCenterMemberClientFallback() {
        return new UCenterMemberClientFallback();
    }
}
