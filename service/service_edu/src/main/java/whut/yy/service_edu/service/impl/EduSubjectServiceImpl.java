package whut.yy.service_edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import whut.yy.service_edu.entity.EduSubject;
import whut.yy.service_edu.entity.SubjectExcel;
import whut.yy.service_edu.entity.vo.SubjectNode;
import whut.yy.service_edu.listener.ExcelListener;
import whut.yy.service_edu.mapper.EduSubjectMapper;
import whut.yy.service_edu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author gagayang
 * @since 2022-11-20
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Autowired
    @Lazy //不加这个注解，会出现循环引用
    private EduSubjectService subjectService;

    @Override
    public void addSubject(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, SubjectExcel.class, new ExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SubjectNode> getSubjectTree() {
        return recursiveSearch("0");
    }

    //递归获取课程分类树形结构
    private List<SubjectNode> recursiveSearch(String id) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<EduSubject> list = baseMapper.selectList(wrapper);
        List<SubjectNode> treeList = new ArrayList<>();
        if (list != null) {
            for (EduSubject eduSubject : list) {
                SubjectNode subjectNode = new SubjectNode();
                BeanUtils.copyProperties(eduSubject, subjectNode);
//                subjectNode.setId(eduSubject.getId());
//                subjectNode.setTitle(eduSubject.getTitle());
                subjectNode.setSon(this.recursiveSearch(eduSubject.getId()));
                treeList.add(subjectNode);
            }
        }
        return treeList;
    }
}
