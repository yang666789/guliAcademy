package whut.yy.service_sms.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Getter
@Setter
public class ConstantPropertiesUtil {

    private String keyId;

    private String keySecret;

}
