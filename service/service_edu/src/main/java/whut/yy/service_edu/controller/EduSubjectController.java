package whut.yy.service_edu.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import whut.yy.common_util.R;
import whut.yy.service_edu.service.EduSubjectService;

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
}

