package whut.yy.service_edu.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "课时信息")
@Data
public class VideoVo {
    private String id;
    private String title;
}
