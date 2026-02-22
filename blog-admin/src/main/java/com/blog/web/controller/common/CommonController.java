package com.blog.web.controller.common;

import com.blog.common.annotation.RateLimiter;
import com.blog.common.constant.CacheConstants;
import com.blog.common.core.controller.BaseController;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.LimitType;
import com.blog.common.utils.file.MimeTypeUtils;
import com.blog.framework.web.service.MinioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @RateLimiter(key = CacheConstants.UPLOAD_IMAGE_KEY, count = 10,limitType = LimitType.IP)
    @PostMapping("/uploadImage")
    public AjaxResult uploadImage(@RequestParam("image") MultipartFile image) throws Exception {
        if (!image.isEmpty()) {
            // 检查文件大小 100MB
            if (image.getSize() > 100 * 1024 * 1024) {
                return error("文件大小超过限制，请上传小于100MB的图片");
            }
            String url = minioService.uploadImage(image, MimeTypeUtils.IMAGE_EXTENSION, true);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("imgUrl", url);
            return ajax;
        }
        return error("上传图片异常");
    }



}
