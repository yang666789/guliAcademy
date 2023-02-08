package whut.yy.service_order.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.ResultCode;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_order.service.PayLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-12-24
 */
@Api(description = "支付日志")
@RestController
@RequestMapping("/service_order/payLog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //跳转到支付宝支付页面
    @GetMapping("/createNative/{orderNo}")
    public String createNative(@PathVariable String orderNo) {
        return payLogService.createNative(orderNo);
    }

    //异步回调接口：支付成功后调用此接口
    @PostMapping("/notify")  // 注意这里必须是POST接口
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        //更新支付状态
        String courseId = payLogService.updateOrderStatus(request);
        //支付完成跳转到课程详情页面
        try {
            response.sendRedirect("http://localhost:3000/course/" + courseId);
        } catch (Exception e) {
            throw new MyGlobalException(ResultCode.ERROR, "重定向到已付款课程页面失败");
        }
    }
}

