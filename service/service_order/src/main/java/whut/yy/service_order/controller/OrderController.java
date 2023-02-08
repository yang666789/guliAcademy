package whut.yy.service_order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.JwtUtils;
import whut.yy.common_util.R;
import whut.yy.service_order.entity.Order;
import whut.yy.service_order.service.OrderService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-12-24
 */
@Api(description = "订单模块")
@RestController
@RequestMapping("/service_order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "根据课程id和用户id创建订单，返回订单id")
    @PostMapping("addOrder/{courseId}")
    public R addOrder(@PathVariable String courseId, HttpServletRequest request) {
        String orderId = orderService.addOrder(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId", orderId);
    }

    @ApiOperation(value = "根据订单号查询订单信息")
    @GetMapping("orderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    //远程调用开发接口：用于查询课程是否支付
    @ApiOperation(value = "远程调用开发接口：用于查询课程是否支付")
    @GetMapping("orderHasPay/{courseId}/{memberId}")
    public boolean orderHasPay(@PathVariable String courseId, @PathVariable String memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);
        return orderService.count(wrapper) > 0;
    }
}

