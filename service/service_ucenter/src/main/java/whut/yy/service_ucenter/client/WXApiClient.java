package whut.yy.service_ucenter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wxClient", url = "https://api.weixin.qq.com/sns")
public interface WXApiClient {

    @GetMapping("oauth2/access_token")
    String getAccessToken(@RequestParam String appid,
                          @RequestParam String secret,
                          @RequestParam String code,
                          @RequestParam String grant_type);

    @GetMapping("userinfo")
    String getUserInfo(@RequestParam String access_token,
                       @RequestParam String openid);
}
