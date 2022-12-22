package whut.yy.service_sms.service;

import java.util.Map;

public interface SmsService {
    boolean sendMessage(String phone, Map<String, Object> param);
}
