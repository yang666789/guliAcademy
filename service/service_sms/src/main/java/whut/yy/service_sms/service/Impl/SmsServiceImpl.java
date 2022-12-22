package whut.yy.service_sms.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whut.yy.service_sms.service.SmsService;
import whut.yy.service_sms.util.ConstantPropertiesUtil;

import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private ConstantPropertiesUtil propertiesUtil;

    @Override
    public boolean sendMessage(String phone, Map<String, Object> param) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai",
                propertiesUtil.getKeyId(), propertiesUtil.getKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSignName("谷粒学院在线网站");
        request.setTemplateCode("SMS_262550583");
        request.setPhoneNumbers(phone);
        request.setTemplateParam(JSONObject.toJSONString(param));

        try {
            client.getAcsResponse(request);
//            SendSmsResponse response = client.getAcsResponse(request);
//            System.out.println("SMS返回消息：" + new Gson().toJson(response));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
