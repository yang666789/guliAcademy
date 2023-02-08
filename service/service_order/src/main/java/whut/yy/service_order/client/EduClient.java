package whut.yy.service_order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-edu")
public interface EduClient {

    @GetMapping("/service_edu/courseFront/courseInfo/{courseId}")
    String getCourseInfoByCourseId(@PathVariable("courseId") String courseId);
}
