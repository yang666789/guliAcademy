package whut.yy.service_ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import whut.yy.common_util.JwtUtils;
import whut.yy.common_util.MD5;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_ucenter.entity.UcenterMember;
import whut.yy.service_ucenter.entity.vo.LoginVo;
import whut.yy.service_ucenter.entity.vo.RegisterVo;
import whut.yy.service_ucenter.mapper.UcenterMemberMapper;
import whut.yy.service_ucenter.service.UcenterMemberService;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2022-12-11
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(LoginVo loginVo) {
        //检查字段非空
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password))
            throw new MyGlobalException(20001, "手机号或密码为空");

        //按手机号查找用户，判断用户非空
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember member = baseMapper.selectOne(wrapper);
        if (member == null)
            throw new MyGlobalException(20001, "不存在该用户");

        //校验加密后密码是否相同
        if (!MD5.encrypt(password).equals(member.getPassword()))
            throw new MyGlobalException(20001, "密码错误");

        //校验是否被禁用
        if (member.getIsDisabled())
            throw new MyGlobalException(20001, "用户已被禁用");

        //生成token
        return JwtUtils.getJwtToken(member.getId(), member.getNickname());
    }

    @Override
    public void register(RegisterVo registerVo) {
        //校验字段非空
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(password) || StringUtils.isEmpty(code))
            throw new MyGlobalException(20001, "昵称、手机号、密码、验证码非空");

        //校验验证码相同
        if (!code.equals(redisTemplate.opsForValue().get(mobile)))
            throw new MyGlobalException(20001, "验证码错误");

        //校验相同手机号用户是否存在
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        if (baseMapper.selectCount(wrapper) > 0)
            throw new MyGlobalException(20001, "用户已存在");

        //数据库中添加用户
        UcenterMember member = new UcenterMember();
        BeanUtils.copyProperties(registerVo, member);
        member.setPassword(MD5.encrypt(registerVo.getPassword()));//加密
        member.setAvatar("http://guli-edu-gagayang.oss-cn-shanghai.aliyuncs.com" +
                "/2022/11/23/05278380-63d3-4e8a-b0e9-78f05bcb4624.png");//设置默认头像
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        return baseMapper.selectOne(wrapper);
    }

    //获取某日用户注册人数
    @Override
    public int getUserRegisterNum(String date) {
        return baseMapper.getUserRegisterNum(date);
    }
}
