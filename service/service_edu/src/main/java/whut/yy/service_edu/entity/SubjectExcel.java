package whut.yy.service_edu.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectExcel {
    @ExcelProperty(index = 0)//设置列对应的属性
    private String firstSubject;
    @ExcelProperty(index = 1)//设置列对应的属性
    private String secondSubject;
}
