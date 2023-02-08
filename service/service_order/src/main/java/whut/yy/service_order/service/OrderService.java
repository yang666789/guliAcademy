package whut.yy.service_order.service;

import whut.yy.service_order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-24
 */
public interface OrderService extends IService<Order> {

    String addOrder(String courseId, String memberId);
}
