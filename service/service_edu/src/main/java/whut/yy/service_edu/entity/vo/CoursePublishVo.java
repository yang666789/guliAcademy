package whut.yy.service_edu.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "课程发布信息")
@Data
public class CoursePublishVo {

    /**
     * Course表
     */
    private String title;
    private String cover;
    private Integer lessonNum;
    private String price;//只用于显示

    /**
     * Subject表
     */
    private String subjectLevelOne;
    private String subjectLevelTwo;

    /**
     * Teacher表
     */
    private String teacherName;
}
