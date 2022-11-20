package whut.yy.service_base.exceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import whut.yy.common_util.R;
import whut.yy.service_base.exception.MyGlobalException;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        return R.error();
    }

    @ExceptionHandler(MyGlobalException.class)
    @ResponseBody
    public R error(MyGlobalException e) {
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
