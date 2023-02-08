package whut.yy.service_acl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whut.yy.service_acl.entity.Permission;
import whut.yy.service_acl.entity.RolePermission;
import whut.yy.service_acl.entity.User;
import whut.yy.service_acl.helper.MemuHelper;
import whut.yy.service_acl.helper.PermissionHelper;
import whut.yy.service_acl.mapper.PermissionMapper;
import whut.yy.service_acl.service.PermissionService;
import whut.yy.service_acl.service.RolePermissionService;
import whut.yy.service_acl.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserService userService;

    //根据角色id获取菜单(生成前端tree结构)
    @Override
    public List<Permission> selectMenuByRoleId(String roleId) {
        //id类型为字符串，转换成整型进行升序排序
        List<Permission> allPermissionList = baseMapper.selectList(
                new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(
                new QueryWrapper<RolePermission>().eq("role_id", roleId));
        for (Permission permission : allPermissionList) {
            for (RolePermission rolePermission : rolePermissionList) {
                if (rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }
        return PermissionHelper.createTreeMenu(allPermissionList, true);
    }

    //根据用户id获取用户按钮菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if (this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    //根据用户id获取用户的权限菜单
    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if (this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        //构建树形菜单
        List<Permission> permissionList = PermissionHelper.createTreeMenu(selectPermissionList, false);
        //构建前端路由菜单
        return MemuHelper.bulid(permissionList);
    }

    /**
     * 判断用户是否系统管理员
     *
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if (null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    //region 递归查询、删除菜单
    //获取树形菜单
    @Override
    public List<Permission> getTreeMenu() {
        //查询所有菜单
        List<Permission> permissionList = baseMapper.selectList(null);

        //处理数据，生成树形菜单
        return PermissionHelper.createTreeMenu(permissionList, true);
    }

    //递归删除菜单方法1：获取所有菜单，再递归找到所有子菜单，包括该节点进行删除
    @Override
    public void removeChildById(String id) {
        //查询所有菜单
        List<Permission> permissionList = baseMapper.selectList(null);

        //处理数据获取所有要删除的菜单id
        List<String> deleteIds = PermissionHelper.searchChildIdsById(permissionList, id);//该菜单下所有子菜单
        deleteIds.add(id);//该菜单也要删除

        //删除指定菜单
        baseMapper.deleteBatchIds(deleteIds);
    }

    //递归删除菜单方法2：递归查询数据库获取当前结点的所有子节点，将结点都加入到结果集中进行删除
    @Override
    public void removeChildByIdGuli(String id) {
        //1 创建list集合，用于封装所有删除菜单id值
        List<String> idList = new ArrayList<>();
        //2 向idList集合设置删除菜单id
        this.selectPermissionChildById(id, idList);
        //把当前id封装到list里面
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //根据当前菜单id，查询菜单里面子菜单id，封装到list集合
    private void selectPermissionChildById(String id, List<String> idList) {
        //查询菜单里面子菜单id
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", id);
        wrapper.select("id");
        List<Permission> childIdList = baseMapper.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getId());
            //递归查询
            this.selectPermissionChildById(item.getId(), idList);
        });
    }

    //给角色分配菜单
    @Override
    public void saveRolePermissionRealtionShip(String roleId, String[] permissionIds) {
        //1.先要删除该角色拥有的原始权限，保证该角色权限更新
        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));

        //roleId角色id
        //permissionId菜单id 数组形式
        //2.创建list集合，用于封装添加数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        //遍历所有菜单数组
        for (String perId : permissionIds) {
            //RolePermission对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(perId);
            //封装到list集合
            rolePermissionList.add(rolePermission);
        }
        //添加到角色菜单关系表
        rolePermissionService.saveBatch(rolePermissionList);
    }
    //endregion
}
