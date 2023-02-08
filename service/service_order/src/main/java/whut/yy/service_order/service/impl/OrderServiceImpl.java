package whut.yy.service_order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whut.yy.service_order.client.EduClient;
import whut.yy.service_order.client.UCenterClient;
import whut.yy.service_order.entity.Order;
import whut.yy.service_order.mapper.OrderMapper;
import whut.yy.service_order.service.OrderService;
import whut.yy.service_order.util.OrderNoUtil;

import java.util.HashMap;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UCenterClient uCenterClient;

    @Autowired
    private EduClient eduClient;

    @Override
    public String addOrder(String courseId, String memberId) {
        Gson gson = new Gson();

        //远程调用根据用户id获取用户信息
        String userInfo = uCenterClient.getLoginInfo(memberId);
        HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);

        //远程调用根据课程id获取课程信息
        String courseInfo = eduClient.getCourseInfoByCourseId(courseId);
        HashMap courseInfoMap = gson.fromJson(courseInfo, HashMap.class);

        //信息封装到order，添加到数据库
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle((String) courseInfoMap.get("title"));
        order.setCourseCover((String) courseInfoMap.get("cover"));
        order.setTeacherName((String) courseInfoMap.get("teacherName"));
        order.setMemberId(memberId);
        order.setNickname((String) userInfoMap.get("nickname"));
        order.setMobile((String) userInfoMap.get("mobile"));

        order.setStatus(0);//订单状态（0：未支付 1：已支付）
        order.setPayType(2);//支付类型（1：微信 2：支付宝）

        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
