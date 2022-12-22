package whut.yy.service_cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cache.annotation.Cacheable;
import whut.yy.service_cms.entity.CmsBanner;
import whut.yy.service_cms.mapper.CmsBannerMapper;
import whut.yy.service_cms.service.CmsBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-04
 */
@Service
public class CmsBannerServiceImpl extends ServiceImpl<CmsBannerMapper, CmsBanner> implements CmsBannerService {

    @Cacheable(value = "banner", key = "'getAllBanners'")
    @Override
    public List<CmsBanner> getAllBanners() {
        QueryWrapper<CmsBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return baseMapper.selectList(wrapper);
    }
}
