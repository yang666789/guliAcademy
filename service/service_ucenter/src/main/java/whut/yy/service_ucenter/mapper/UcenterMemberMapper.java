package whut.yy.service_ucenter.mapper;

import whut.yy.service_ucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author gagayang
 * @since 2022-12-11
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    int getUserRegisterNum(String date);
}
