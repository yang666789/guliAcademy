package whut.yy.service_statistic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whut.yy.common_util.ResultCode;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_statistic.client.UCenterMemberClient;
import whut.yy.service_statistic.entity.StatisticsDaily;
import whut.yy.service_statistic.entity.dto.StatisticsDto;
import whut.yy.service_statistic.mapper.StatisticsDailyMapper;
import whut.yy.service_statistic.service.StatisticsDailyService;
import whut.yy.service_statistic.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2023-01-30
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UCenterMemberClient memberClient;

    //统计某天的数据
    @Override
    public void generateStatistics(String date) {
        //每日数据动态变化，所以新增之前删除已存在的当日数据，保证当日数据为最新
        //先删除当日数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", date);
        baseMapper.delete(wrapper);

        //新增当日数据
        StatisticsDaily daily = new StatisticsDaily();
        daily.setDateCalculated(date);
        daily.setRegisterNum(memberClient.getUserRegisterNum(date));
        daily.setLoginNum(RandomUtils.nextInt(100, 200));//随机数
        daily.setVideoViewNum(RandomUtils.nextInt(100, 200));
        daily.setCourseNum(RandomUtils.nextInt(100, 200));
        baseMapper.insert(daily);
    }

    //获取某时间段某类型的统计数据
    @Override
    public Map<String, Object> getStatisticsOfTypeAndTime(StatisticsDto statisticsDto) {
        //1.按条件查询
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",
                statisticsDto.getStartDate(), statisticsDto.getEndDate());
        wrapper.select("date_calculated", statisticsDto.getType());//要保证type的值和数据库表的字段名一一对应
        List<StatisticsDaily> statistics = baseMapper.selectList(wrapper);

        //2.根据前端echarts需要格式处理数据
        Map<String, Object> res = new HashMap<>();//结果map
        List<String> xData = new ArrayList<>();//x轴数组
        List<Integer> yData = new ArrayList<>();//y轴数据

        try {
            for (StatisticsDaily daily : statistics) {
                xData.add(daily.getDateCalculated());

                //通过反射调用 getXXX 方法获取值或者用 switch
                //方法1：
//                PropertyDescriptor propertyDescriptor =
//                        new PropertyDescriptor(
//                                StringUtils.underline2Camel(statisticsDto.getType(),false),
//                                daily.getClass());
//                Integer fieldValue = (Integer) propertyDescriptor.getReadMethod().invoke(daily);
                //方法2：
                String methodName = "get" +
                        StringUtils.underline2Camel(statisticsDto.getType(), false);
                Integer fieldValue = (Integer) daily.getClass().getMethod(methodName).invoke(daily);

                yData.add(fieldValue);
            }
        } catch (Exception e) {
            throw new MyGlobalException(ResultCode.ERROR, e.getMessage());
        }

        res.put("xData", xData);
        res.put("yData", yData);
        return res;
    }
}
