package whut.yy.service_edu.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import whut.yy.service_edu.entity.EduTeacher;
import whut.yy.service_edu.service.EduTeacherService;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-11-02
 */
@RestController
@RequestMapping("/service_edu/teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    //地址：http://localhost:8001/service_edu/teacher/teachers
    //查询所有老师
    @GetMapping("teachers")
    public List<EduTeacher> getAll() {
        return eduTeacherService.list(null);
    }

    //根据 id 删除老师
    @DeleteMapping("{id}")
    public boolean deleteById(@PathVariable String id) {
        return eduTeacherService.removeById(id);
    }
}

