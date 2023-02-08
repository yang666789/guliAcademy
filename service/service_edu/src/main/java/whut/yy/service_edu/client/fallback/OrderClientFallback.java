package whut.yy.service_edu.client.fallback;

import whut.yy.common_util.ResultCode;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_edu.client.OrderClient;

public class OrderClientFallback implements OrderClient {
    @Override
    public boolean orderHasPay(String courseId, String memberId) {
        throw new MyGlobalException(ResultCode.ERROR, "service-order 服务挂了！");
    }
}
