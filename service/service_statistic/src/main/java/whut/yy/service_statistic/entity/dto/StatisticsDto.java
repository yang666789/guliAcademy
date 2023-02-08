package whut.yy.service_statistic.entity.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    private String type;//统计类型
    private String startDate;//开始日期
    private String endDate;//结束日期
}
