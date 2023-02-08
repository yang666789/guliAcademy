package whut.yy.service_order.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whut.yy.common_util.ResultCode;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_order.entity.Order;
import whut.yy.service_order.entity.PayLog;
import whut.yy.service_order.mapper.PayLogMapper;
import whut.yy.service_order.service.OrderService;
import whut.yy.service_order.service.PayLogService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-24
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //跳转到支付宝支付页面
    @Override
    public String createNative(String orderNo) {
        //根据订单id获取订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);
        //订单不存在
        if (order == null) {
            throw new MyGlobalException(ResultCode.ERROR, "订单号错误，不存在这样的订单");
        }


        //设置支付参数
        AlipayTradePagePayResponse response;
        try {
            //发起API调用（以创建当面付收款二维码为例）
            response = Factory.Payment.Page()
                    .pay(order.getCourseTitle() + ":" + order.getCourseId(),
                            order.getOrderNo(),
                            order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "",
                            "");
        } catch (Exception e) {
            throw new MyGlobalException(ResultCode.ERROR, e.getMessage());
        }
        return response.getBody();
    }

    /**
     * 更新订单
     *
     * @param request
     * @return 付费课程的课程编号
     */
    @Override
    public String updateOrderStatus(HttpServletRequest request) {
        //支付成功
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            try {
                // 支付宝验签
                if (Factory.Payment.Common().verifyNotify(params)) {
                    // 验签通过
//                System.out.println("(课程名称:课程编号)" + params.get("subject"));
//                System.out.println("交易状态: " + params.get("trade_status"));
//                System.out.println("商户订单号: " + params.get("out_trade_no"));
//                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                System.out.println("买家订单创建时间（未付款）: " + params.get("gmt_create"));
//                System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
//                System.out.println("交易金额: " + params.get("total_amount"));
//                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));

                    // 更新订单为已支付
                    //1.更新order表为已支付
                    String orderNo = params.get("out_trade_no");
                    QueryWrapper<Order> wrapper = new QueryWrapper<>();
                    wrapper.eq("order_no", orderNo);
                    Order order = orderService.getOne(wrapper);
                    order.setStatus(1);//设置为已支付
                    orderService.updateById(order);

                    //2.添加paylog表
                    PayLog payLog = new PayLog();
                    payLog.setOrderNo(params.get("out_trade_no"));
                    payLog.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .parse(params.get("gmt_payment")));
                    payLog.setTotalFee(new BigDecimal(params.get("total_amount")));
                    payLog.setTransactionId(params.get("trade_no"));//流水号为支付宝交易凭证号
                    payLog.setTradeState(params.get("trade_status"));
                    payLog.setPayType(2);//1微信，2支付宝
                    baseMapper.insert(payLog);

                    return params.get("subject").split(":")[1];//返回付费课程编号
                }
            } catch (Exception e) {
                throw new MyGlobalException(ResultCode.ERROR, e.getMessage());
            }
        }
        return "";
    }
}
