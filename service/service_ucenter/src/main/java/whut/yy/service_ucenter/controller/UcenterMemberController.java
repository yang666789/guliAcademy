package whut.yy.service_ucenter.controller;


import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import whut.yy.common_util.JwtUtils;
import whut.yy.common_util.R;
import whut.yy.service_ucenter.entity.UcenterMember;
import whut.yy.service_ucenter.entity.vo.LoginVo;
import whut.yy.service_ucenter.entity.vo.RegisterVo;
import whut.yy.service_ucenter.service.UcenterMemberService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-12-11
 */
@Api(description = "用户中心")
@RestController
@RequestMapping("/ucenter/member")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("item", member);
    }

    @ApiOperation(value = "根据用户id获取用户信息")
    @GetMapping("getUserInfo/{memberId}")
    public String getLoginInfo(@PathVariable String memberId) {
        UcenterMember member = memberService.getById(memberId);
        Gson gson = new Gson();
        return gson.toJson(member);
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }
}

