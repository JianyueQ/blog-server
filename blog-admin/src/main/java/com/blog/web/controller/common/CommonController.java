package com.blog.web.controller.common;

import com.blog.common.core.controller.BaseController;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.domain.AjaxResult;
import com.blog.common.exception.file.FileUploadException;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.file.MimeTypeUtils;
import com.blog.framework.web.service.MinioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import static com.blog.common.utils.SecurityUtils.getLoginUserOnAdmin;

/**
 * 通用请求处理
 *
 * @author 31373
 */
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {

    private final MinioService minioService;

    public CommonController(MinioService minioService) {
        this.minioService = minioService;
    }

    /**
     * 通用图片上传-单个
     */
    @PostMapping("/uploadImage")
    public AjaxResult uploadImage(@RequestParam("image") MultipartFile image) throws Exception {
        if (!image.isEmpty()) {
            //上传头像
            String url = minioService.uploadImage(image, MimeTypeUtils.IMAGE_EXTENSION, true);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("imgUrl", url);
            return ajax;
        }
        return error("上传图片异常");
    }



}
