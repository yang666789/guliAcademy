package whut.yy.service_base.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyGlobalException extends RuntimeException {
    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String msg;
}
