package whut.yy.service_acl.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import whut.yy.service_acl.entity.Permission;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface PermissionService extends IService<Permission> {

    //根据角色id获取菜单
    List<Permission> selectMenuByRoleId(String roleId);

    //根据用户id获取用户按钮权限
    List<String> selectPermissionValueByUserId(String id);

    //根据用户id获取用户菜单权限
    List<JSONObject> selectPermissionByUserId(String id);

    //递归删除菜单
    void removeChildByIdGuli(String id);

    //region 递归查询、删除菜单，给角色分配权限
    //获取树形菜单
    List<Permission> getTreeMenu();

    //递归删除菜单
    void removeChildById(String id);

    //给角色分配权限
    void saveRolePermissionRealtionShip(String roleId, String[] permissionId);
    //endregion

}
