package whut.yy.service_edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import whut.yy.common_util.R;
import whut.yy.service_edu.entity.EduTeacher;
import whut.yy.service_edu.entity.vo.TeacherQuery;
import whut.yy.service_edu.service.EduTeacherService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-11-02
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/service_edu/teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    //地址：http://localhost:8001/service_edu/teacher/teachers
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("teachers")
    public R getAll() {
        List<EduTeacher> teacherList = eduTeacherService.list(null);
        return R.ok().data("items", teacherList);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R deleteById(@ApiParam(name = "id", value = "讲师ID", required = true)
                        @PathVariable String id) {
        boolean isSuccess = eduTeacherService.removeById(id);
        if (isSuccess)
            return R.ok();
        else return R.error();
    }

    @ApiOperation(value = "分页查询讲师")
    @GetMapping("{page}/{limit}")
    public R pageList(@PathVariable long page,
                      @PathVariable long limit) {
        //构造 Page 对象
        Page<EduTeacher> pageList = new Page<>(page, limit);
        //分页数据封装到 Page 对象中
        eduTeacherService.page(pageList, null);
        //构造返回数据
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("total", pageList.getTotal());
        dataMap.put("rows", pageList.getRecords());
        return R.ok().data(dataMap);
    }

    @ApiOperation(value = "多条件查询(分页)")
    @PostMapping("pageList/{page}/{limit}")
    public R pageList(@PathVariable long page,
                      @PathVariable long limit,
                      @RequestBody(required = false) TeacherQuery teacherQuery) {
        //构造 Page 对象
        Page<EduTeacher> pageList = new Page<>(page, limit);

        //构造条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        if (!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(begin)) {
            //注意传递的是表中的字段名，而不是实体类的字段名
            queryWrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end);
        }

        queryWrapper.orderByDesc("gmt_create");

        //分页数据封装到 Page 对象中
        eduTeacherService.page(pageList, queryWrapper);

        //构造返回数据
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("total", pageList.getTotal());
        dataMap.put("rows", pageList.getRecords());
        return R.ok().data(dataMap);
    }

    @ApiOperation(value = "添加讲师")
    @PostMapping()
    public R add(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else return R.error();
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("item", teacher);
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping
    public R updateById(@ApiParam(name = "teacher", value = "讲师对象", required = true)
                        @RequestBody EduTeacher teacher) {
        eduTeacherService.updateById(teacher);
        return R.ok();
    }
}

