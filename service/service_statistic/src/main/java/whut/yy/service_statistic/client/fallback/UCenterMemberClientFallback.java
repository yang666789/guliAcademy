package whut.yy.service_statistic.client.fallback;

import whut.yy.common_util.ResultCode;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_statistic.client.UCenterMemberClient;

public class UCenterMemberClientFallback implements UCenterMemberClient {
    @Override
    public int getUserRegisterNum(String date) {
        throw new MyGlobalException(ResultCode.ERROR, "用户中心服务网络异常");
    }
}
