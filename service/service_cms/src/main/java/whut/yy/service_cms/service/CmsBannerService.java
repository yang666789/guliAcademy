package whut.yy.service_cms.service;

import whut.yy.service_cms.entity.CmsBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-04
 */
public interface CmsBannerService extends IService<CmsBanner> {

    List<CmsBanner> getAllBanners();
}
