package com.blog.common.utils.ip;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.blog.common.constant.Constants;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 *
 * @author 31373
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询 - 使用HTTPS
    public static final String IP_URL = "https://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = HttpUtils.sendGet(IP_URL, "json=true" + "&ip=" + ip, Constants.GBK);
            if (StringUtils.isEmpty(rspStr))
            {
                log.error("获取地理位置异常，IP: {}", ip);
                return UNKNOWN;
            }

            // 处理JSONP格式，提取JSON部分
            // 格式: IPCallBack({"ip":"x.x.x.x","pro":"xx",...})
            if (rspStr.contains("IPCallBack")) {
                int start = rspStr.indexOf("(");
                int end = rspStr.lastIndexOf(")");
                if (start > 0 && end > start) {
                    rspStr = rspStr.substring(start + 1, end);
                }
            }

            JSONObject obj = JSON.parseObject(rspStr);


            String region = obj.getString("pro");
            String city = obj.getString("city");

            // 如果省份和城市为空，返回 addr 字段
            if (StringUtils.isEmpty(region) && StringUtils.isEmpty(city)) {
                String addr = obj.getString("addr");
                return StringUtils.isNotEmpty(addr) ? addr : UNKNOWN;
            }

            return String.format("%s %s", region, city);
        } catch (Exception e) {
            log.error("获取地理位置异常，IP: {}, 错误: {}", ip, e.getMessage());
        }
        return UNKNOWN;
    }
}