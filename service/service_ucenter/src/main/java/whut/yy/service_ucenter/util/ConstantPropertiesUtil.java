package whut.yy.service_ucenter.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx.open")
@Getter
@Setter
public class ConstantPropertiesUtil {

    private String appId;

    private String appSecret;

    private String redirectUrl;

}
