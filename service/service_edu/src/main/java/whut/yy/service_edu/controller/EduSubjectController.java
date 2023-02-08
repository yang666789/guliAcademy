package whut.yy.service_edu.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import whut.yy.common_util.R;
import whut.yy.service_edu.entity.vo.SubjectNode;
import whut.yy.service_edu.service.EduSubjectService;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-11-20
 */
@Api(description = "课程分类")
@RestController
@RequestMapping("/service_edu/subject")
public class EduSubjectController {

    @Autowired
    EduSubjectService subjectService;

    @ApiOperation(value = "上传课程分类文件")
    @PostMapping
    public R addSubject(@ApiParam(required = true) MultipartFile file) {
        subjectService.addSubject(file);
        return R.ok();
    }

    @ApiOperation(value = "获取课程分类树形结构")
    @GetMapping
    public R getTree() {
        List<SubjectNode> subjectNode = subjectService.getSubjectTree();
        return R.ok().data("list", subjectNode);
    }
}

