package whut.yy.service_statistic.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.R;
import whut.yy.service_statistic.entity.dto.StatisticsDto;
import whut.yy.service_statistic.service.StatisticsDailyService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2023-01-30
 */
@Api(value = "数据统计接口")
@RestController
@RequestMapping("/service_statistic/statistics-daily")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsService;

    @ApiOperation(value = "生成某天的统计数据")
    @PostMapping("statistics/{date}")
    public R generateStatistics(@PathVariable String date) {
        statisticsService.generateStatistics(date);
        return R.ok();
    }

    @ApiOperation(value = "获取某时间段某类型的统计数据")
    @PostMapping("statisticsOfTypeAndTime")
    public R getStatisticsOfTypeAndTime(@RequestBody StatisticsDto statisticsDto) {
        Map<String, Object> statisticsData = statisticsService.getStatisticsOfTypeAndTime(statisticsDto);
        return R.ok().data(statisticsData);
    }
}

