package com.blog.system.service.serviceImpl;

import com.blog.common.constant.CacheConstants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.uuid.IdUtils;
import com.blog.system.domain.vo.BlogOnerProfileVO;
import com.blog.system.mapper.UserProfileMapper;
import com.blog.system.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 *  前台用户信息处理
 *
 * @author 31373
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    public static final Integer CACHE_BLOG_OWNER_PROFILE_EXPIRE = 1;

    private final UserProfileMapper userProfileMapper;
    private final RedisCache redisCache;

    public UserProfileServiceImpl(UserProfileMapper userProfileMapper, RedisCache redisCache) {
        this.userProfileMapper = userProfileMapper;
        this.redisCache = redisCache;
    }

    @Override
    public BlogOnerProfileVO getBlogOwnerProfile() {
        // 从缓存中获取博客作者信息
        BlogOnerProfileVO blogOwnerProfileCache = redisCache.getCacheObject(CacheConstants.CACHE_BLOG_OWNER_PROFILE);
        if (StringUtils.isNotNull(blogOwnerProfileCache)){
            return blogOwnerProfileCache;
        }
        BlogOnerProfileVO blogOwnerProfile = userProfileMapper.getBlogOwnerProfile();
        String uuid = IdUtils.fastSimpleUUID();
        blogOwnerProfile.setUuid(uuid);
        //解析ip地址;
        String realAddressByIp = AddressUtils.getRealAddressByIP(blogOwnerProfile.getLastLoginIp());
        blogOwnerProfile.setLastLoginIp(realAddressByIp);
        redisCache.setCacheObject(CacheConstants.CACHE_BLOG_OWNER_PROFILE, blogOwnerProfile, CACHE_BLOG_OWNER_PROFILE_EXPIRE, TimeUnit.HOURS);
        return blogOwnerProfile;
    }
}
