package com.blog.business.controller.front;

import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.service.VisitorRecordService;
import com.blog.common.annotation.Anonymous;
import com.blog.common.core.controller.BaseController;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访客记录控制器
 *
 * @author 31373
 */
@Anonymous
@RestController("frontVisitorRecordController")
@RequestMapping("/blog/visitor/record")
public class VisitorRecordController extends BaseController {

    @Autowired
    private VisitorRecordService visitorRecordService;

    /**
     * 访客获取唯一的哈希指纹
     */
    @GetMapping
    public AjaxResult getPermissions(String clientData) {
        return AjaxResult.success(visitorRecordService.getPermissions(clientData));
    }

}
