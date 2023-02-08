package whut.yy.service_edu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whut.yy.common_util.JwtUtils;
import whut.yy.common_util.R;
import whut.yy.service_edu.client.UCenterClient;
import whut.yy.service_edu.entity.EduComment;
import whut.yy.service_edu.service.EduCommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-12-21
 */
@Api(description = "课程评论")
@RestController
@RequestMapping("/service_edu/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UCenterClient uCenterClient;

    @ApiOperation(value = "获取课程评论分页信息")
    @GetMapping("page/{courseId}/{page}/{limit}")
    public R getCommentPageInfo(@PathVariable String courseId,
                                @PathVariable long page,
                                @PathVariable long limit) {
        Page<EduComment> commentPage = new Page<>(page, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        commentService.page(commentPage, wrapper);

        List<EduComment> commentList = commentPage.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", commentPage.getCurrent());
        map.put("pages", commentPage.getPages());
        map.put("size", commentPage.getSize());
        map.put("total", commentPage.getTotal());
        map.put("hasNext", commentPage.hasNext());
        map.put("hasPrevious", commentPage.hasPrevious());
        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论(comment不含评论人信息，评论人信息从request中携带的token取到)")
    @PostMapping("addComment")
    public R addComment(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        String userInfo = uCenterClient.getLoginInfo(memberId);
        Gson gson = new Gson();
        HashMap map = gson.fromJson(userInfo, HashMap.class);
        comment.setMemberId(memberId);
        comment.setNickname((String) map.get("nickname"));
        comment.setAvatar((String) map.get("avatar"));
        return commentService.save(comment) ? R.ok() : R.error();
    }
}

