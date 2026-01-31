package com.blog.web.controller.common;

import com.blog.common.domain.AjaxResult;
import com.blog.common.exception.file.FileUploadException;
import com.blog.framework.web.service.MinioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用请求处理
 *
 * @author 31373
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    private final MinioService minioService;

    public CommonController(MinioService minioService) {
        this.minioService = minioService;
    }

    /**
     * 通用文件上传
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String fileName = minioService.uploadFile(file);
            // 上传并返回新文件名称

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", fileName);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", getFileNameFromUrl(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    private Object getFileNameFromUrl(String url) {
        if (url != null && url.contains("/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return url;
    }

}
