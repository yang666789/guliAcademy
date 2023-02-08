package whut.yy.service_ucenter.service;

import whut.yy.service_ucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import whut.yy.service_ucenter.entity.vo.LoginVo;
import whut.yy.service_ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-11
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    UcenterMember getByOpenid(String openid);

    int getUserRegisterNum(String date);
}
