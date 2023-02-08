package whut.yy.service_acl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import whut.yy.service_acl.entity.Permission;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface PermissionMapper extends BaseMapper<Permission> {


    //根据用户id获取按钮级别权限
    List<String> selectPermissionValueByUserId(String id);

    //获取所有按钮级别权限
    List<String> selectAllPermissionValue();

    //根据用户id获取用户的权限菜单
    List<Permission> selectPermissionByUserId(String userId);
}
