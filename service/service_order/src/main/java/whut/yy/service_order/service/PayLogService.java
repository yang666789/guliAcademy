package whut.yy.service_order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import whut.yy.service_order.entity.PayLog;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-24
 */
public interface PayLogService extends IService<PayLog> {

    String createNative(String orderNo);

    String updateOrderStatus(HttpServletRequest request);
}
