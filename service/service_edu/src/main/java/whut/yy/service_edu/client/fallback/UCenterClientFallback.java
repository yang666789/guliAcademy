package whut.yy.service_edu.client.fallback;

import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_edu.client.UCenterClient;

public class UCenterClientFallback implements UCenterClient {
    @Override
    public String getLoginInfo(String memberId) {
        throw new MyGlobalException(20001, "UCenter服务挂了！");
    }
}
