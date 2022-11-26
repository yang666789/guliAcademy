package whut.yy.service_edu.entity.vo;

import lombok.Data;

import java.util.List;

//树形结构
@Data
public class SubjectNode {
    private String id;//课程id
    private String title;//课程分类名称
    private List<SubjectNode> son;//属于该分类下的课程
}
