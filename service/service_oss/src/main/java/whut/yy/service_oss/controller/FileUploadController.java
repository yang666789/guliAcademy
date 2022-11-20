package whut.yy.service_oss.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import whut.yy.common_util.R;
import whut.yy.service_oss.service.FileService;

@Api(description = "阿里云文件管理")
@RestController
@RequestMapping("/service_oss/file")
@CrossOrigin //跨域
public class FileUploadController {

    @Autowired
    FileService fileService;

    @ApiOperation("上传文件")
    @PostMapping
    public R upload(MultipartFile file) {
        String uploadUrl = fileService.upload(file);
        return R.ok().message("文件上传成功").data("url", uploadUrl);
    }
}

