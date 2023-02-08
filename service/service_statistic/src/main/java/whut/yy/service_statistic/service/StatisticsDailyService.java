package whut.yy.service_statistic.service;

import whut.yy.service_statistic.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;
import whut.yy.service_statistic.entity.dto.StatisticsDto;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author gagayang
 * @since 2023-01-30
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void generateStatistics(String date);

    Map<String, Object> getStatisticsOfTypeAndTime(StatisticsDto statisticsDto);
}
