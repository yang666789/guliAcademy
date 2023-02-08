package whut.yy.service_acl.helper;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import whut.yy.service_acl.entity.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建登录用户左侧菜单数据
 * </p>
 *
 * @author qy
 * @since 2019-11-11
 */
public class MemuHelper {

    /**
     * 构建前端路由菜单
     *
     * @param treeNodes:一级菜单列表
     * @return
     */
    public static List<JSONObject> bulid(List<Permission> treeNodes) {
        List<JSONObject> meuns = new ArrayList<>();
        //左侧一级菜单
        for (Permission one : treeNodes) {
            JSONObject oneMeun = new JSONObject();
            oneMeun.put("path", one.getPath());
            oneMeun.put("component", one.getComponent());
            oneMeun.put("redirect", "noredirect");
            oneMeun.put("name", "name_" + one.getId());
            oneMeun.put("hidden", false);

            JSONObject oneMeta = new JSONObject();
            oneMeta.put("title", one.getName());
            oneMeta.put("icon", one.getIcon());
            oneMeun.put("meta", oneMeta);

            List<JSONObject> children = new ArrayList<>();
            List<Permission> twoMeunList = one.getChildren();
            for (Permission two : twoMeunList) {
                JSONObject twoMeun = new JSONObject();
                twoMeun.put("path", two.getPath());
                twoMeun.put("component", two.getComponent());
                twoMeun.put("name", "name_" + two.getId());
                twoMeun.put("hidden", false);

                JSONObject twoMeta = new JSONObject();
                twoMeta.put("title", two.getName());
                twoMeta.put("icon", two.getIcon());
                twoMeun.put("meta", twoMeta);

                children.add(twoMeun);

                //三级菜单是按钮，只不过和二级菜单同级显示，但隐藏了
                //比如讲师列表是二级菜单，与它同级的有删除讲师
                List<Permission> threeMeunList = two.getChildren();
                for (Permission three : threeMeunList) {
                    if (StringUtils.isEmpty(three.getPath())) continue;

                    JSONObject threeMeun = new JSONObject();
                    threeMeun.put("path", three.getPath());
                    threeMeun.put("component", three.getComponent());
                    threeMeun.put("name", "name_" + three.getId());
                    threeMeun.put("hidden", true);

                    JSONObject threeMeta = new JSONObject();
                    threeMeta.put("title", three.getName());
                    threeMeun.put("meta", threeMeta);

                    children.add(threeMeun);
                }
            }
            oneMeun.put("children", children);
            meuns.add(oneMeun);
        }
        return meuns;
    }
}
