package whut.yy.service_edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import whut.yy.service_base.exception.MyGlobalException;
import whut.yy.service_edu.entity.EduSubject;
import whut.yy.service_edu.entity.SubjectExcel;
import whut.yy.service_edu.service.EduSubjectService;

//创建读取excel监听器
public class ExcelListener extends AnalysisEventListener<SubjectExcel> {

    private EduSubjectService subjectService;

    public ExcelListener() {
    }

    //构造函数法注入service
    public ExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    //一行一行去读取excle内容
    @Override
    public void invoke(SubjectExcel subjectExcel, AnalysisContext analysisContext) {
        if (subjectExcel == null) {
            throw new MyGlobalException(20001, "文件数据为空");
        }

        //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(subjectExcel.getFirstSubject());
        if (existOneSubject == null) { //没有相同一级分类，进行添加
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectExcel.getFirstSubject());//一级分类名称
            subjectService.save(existOneSubject);
        }

        //获取一级分类id值
        String pid = existOneSubject.getId();

        //添加二级分类
        //判断二级分类是否重复
        EduSubject existTwoSubject = this.existTwoSubject(subjectExcel.getSecondSubject(), pid);
        if (existTwoSubject == null) {
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectExcel.getSecondSubject());//二级分类名称
            subjectService.save(existTwoSubject);
        }
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        return subjectService.getOne(wrapper);
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(String name, String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        return subjectService.getOne(wrapper);
    }

}
