package whut.yy.service_edu.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.R;

@Api(description = "登录管理")
@RestController
@RequestMapping("/service_edu/user")
public class EduLoginController {

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R login() {
        return R.ok().data("token", "admin");
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("getInfo")
    public R getInfo() {
        return R.ok().data("name", "admin")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
