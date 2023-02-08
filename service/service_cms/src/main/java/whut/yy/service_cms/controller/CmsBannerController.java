package whut.yy.service_cms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import whut.yy.common_util.R;
import whut.yy.service_cms.entity.CmsBanner;
import whut.yy.service_cms.service.CmsBannerService;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author gagayang
 * @since 2022-12-04
 */
@Api(description = "轮播图相关")
@RestController
@RequestMapping("/service_cms/banner")
public class CmsBannerController {

    @Autowired
    private CmsBannerService bannerService;

    //region 前台Banner接口
    @ApiOperation(value = "获取首页banner(前台)")
    @GetMapping("allBanners")
    public R index() {
        List<CmsBanner> list = bannerService.getAllBanners();
        return R.ok().data("bannerList", list);
    }
    //endregion

    //region 后台Banner接口
    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {

        Page<CmsBanner> pageParam = new Page<>(page, limit);
        bannerService.page(pageParam, null);
        return R.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }

    @ApiOperation(value = "新增Banner")
    @PostMapping
    public R save(@RequestBody CmsBanner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("{id}")
    public R get(@PathVariable String id) {
        CmsBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping
    public R updateById(@RequestBody CmsBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("{id}")
    public R remove(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }
    //endregion
}

