package whut.yy.service_ucenter.controller;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import whut.yy.common_util.JwtUtils;
import whut.yy.service_ucenter.client.WXApiClient;
import whut.yy.service_ucenter.entity.UcenterMember;
import whut.yy.service_ucenter.service.UcenterMemberService;
import whut.yy.service_ucenter.util.ConstantPropertiesUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Api(description = "微信登录")
@Controller //注意这里没有配置 @RestController,因为要解析成重定向
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class WxApiController {

    @Autowired
    private ConstantPropertiesUtil propertiesUtil;

    @Autowired
    private WXApiClient wxApiClient;

    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "跳转微信扫码登录页面")
    @GetMapping("login")
    public String toWXQRCodePage() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = propertiesUtil.getRedirectUrl(); //获取业务服务器重定向地址
        redirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8); //url编码

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "gagayang";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名

        //生成二维码链接
        String qrcodeUrl = String.format(
                baseUrl,
                propertiesUtil.getAppId(),
                redirectUrl,
                state);

        return "redirect:" + qrcodeUrl;
    }

    /**
     * @param code:授权临时票据
     * @param state
     * @return
     */
    @ApiOperation(value = "微信扫码登录回调方法")
    @GetMapping("callback")
    public String callback(String code, String state) {
        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //region 方法一：httpclient方法请求数据
        //向认证服务器发送请求换取access_token
//        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
//                "?appid=%s" +
//                "&secret=%s" +
//                "&code=%s" +
//                "&grant_type=authorization_code";
//
//        String accessTokenUrl = String.format(baseAccessTokenUrl,
//                propertiesUtil.getAppId(),
//                propertiesUtil.getAppSecret(),
//                code);
//
//        String result = null;
//        try {
//            result = HttpClientUtils.get(accessTokenUrl);
//            System.out.println("accessToken=============" + result);
//        } catch (Exception e) {
//            throw new GuliException(20001, "获取access_token失败");
//        }
//
//        //解析json字符串
//        Gson gson = new Gson();
//        HashMap map = gson.fromJson(result, HashMap.class);
//        String accessToken = (String) map.get("access_token");
//        String openid = (String) map.get("openid");
//
//        //查询数据库当前用用户是否曾经使用过微信登录
//        Member member = memberService.getByOpenid(openid);
//        if (member == null) {
//            System.out.println("新用户注册");
//
//            //访问微信的资源服务器，获取用户信息
//            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
//                    "?access_token=%s" +
//                    "&openid=%s";
//            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
//            String resultUserInfo = null;
//            try {
//                resultUserInfo = HttpClientUtils.get(userInfoUrl);
//                System.out.println("resultUserInfo==========" + resultUserInfo);
//            } catch (Exception e) {
//                throw new GuliException(20001, "获取用户信息失败");
//            }
//
//            //解析json
//            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
//            String nickname = (String) mapUserInfo.get("nickname");
//            String headimgurl = (String) mapUserInfo.get("headimgurl");
//
//            //向数据库中插入一条记录
//            member = new Member();
//            member.setNickname(nickname);
//            member.setOpenid(openid);
//            member.setAvatar(headimgurl);
//            memberService.save(member);
//        }
        //endregion

        //region 方法二：openfeign方法请求数据
        //获取accessToken、openId
        String accessTokenResult = wxApiClient.getAccessToken(propertiesUtil.getAppId(),
                propertiesUtil.getAppSecret(), code, "authorization_code");
        //解析json字符串
        Gson gson = new Gson();
        HashMap map = gson.fromJson(accessTokenResult, HashMap.class);
        String accessToken = (String) map.get("access_token");
        String openid = (String) map.get("openid");

        //查询数据库当前用用户是否曾经使用过微信登录
        UcenterMember member = memberService.getByOpenid(openid);
        if (member == null) {
            //获取微信登录用户信息
            String userInfoResult = wxApiClient.getUserInfo(accessToken, openid);
            //解析json字符串
            HashMap mapUserInfo = gson.fromJson(userInfoResult, HashMap.class);
            String nickname = (String) mapUserInfo.get("nickname");
            String headimgurl = (String) mapUserInfo.get("headimgurl");

            //保存到数据库中
            member = new UcenterMember();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            memberService.save(member);//插入成功后，member中肯定回写了主键id
        }
        //endregion

        //生成token，并附带到路径上返回
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        //因为端口号不同存在跨域问题，cookie不能跨域，所以这里使用url重写
        return "redirect:http://localhost:3000?token=" + token;
    }
}
