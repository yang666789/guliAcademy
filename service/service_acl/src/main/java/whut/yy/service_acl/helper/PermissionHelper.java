package whut.yy.service_acl.helper;

import whut.yy.service_acl.entity.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建菜单数据
 * </p>
 *
 * @author qy
 * @since 2019-11-11
 */
public class PermissionHelper {

    /**
     * 获取权限菜单
     * 1.获取所有权限菜单：从所有菜单顶级开始构造树形结构，组成一棵树
     * 2.获取部分权限菜单：比如该角色有课程分类管理、课程管理菜单，每个管理为一颗树，组成一个森林
     *
     * @param permissionList
     * @param isAllPermission：是否获取所有权限的树形菜单
     * @return
     */
    public static List<Permission> createTreeMenu(List<Permission> permissionList, boolean isAllPermission) {
        //获取所有权限的树形菜单
        if (isAllPermission) {
            return createTreeStruct(permissionList, "0", 1);
        }
        //获取部分权限的森林菜单
        List<Permission> res = new ArrayList<>();
        List<Permission> firstNodeList = getFirstNodeList(permissionList);
        firstNodeList.forEach(node -> {
            node.setChildren(createTreeStruct(permissionList, node.getId(), 1));
            res.add(node);
        });
        return res;
    }

    /**
     * 根据根节点生成树形结构
     * 但并不包含 id==pid 的节点，如果是 pid==0 是获取所有结点
     *
     * @param permissionList：待转换的结构列表
     * @param pid：父id
     * @param level：层数
     * @return：树形结构
     */
    private static List<Permission> createTreeStruct(List<Permission> permissionList, String pid, int level) {
        List<Permission> res = new ArrayList<>();
        permissionList.forEach(node -> {
            if (pid.equals(node.getPid())) {
                node.setLevel(level);
                node.setChildren(createTreeStruct(permissionList, node.getId(), level + 1));
                res.add(node);
            }
        });
        return res;
    }

    //获取一级结点列表
    private static List<Permission> getFirstNodeList(List<Permission> permissionList) {
        List<Permission> res = new ArrayList<>();
        permissionList.forEach(node -> {
            if ("1".equals(node.getPid())) {
                res.add(node);
            }
        });
        return res;
    }

    /**
     * 根据节点id获取其下所有子节点id（不含 pid 结点）
     *
     * @param permissionList
     * @param pid
     * @return：所有子节点id
     */
    public static List<String> searchChildIdsById(List<Permission> permissionList, String pid) {
        List<String> res = new ArrayList<>();
        permissionList.forEach(node -> {
            if (pid.equals(node.getPid())) {
                List<String> childIds = searchChildIdsById(permissionList, node.getId());
                res.add(node.getId());
                res.addAll(childIds);
            }
        });
        return res;
    }
}
