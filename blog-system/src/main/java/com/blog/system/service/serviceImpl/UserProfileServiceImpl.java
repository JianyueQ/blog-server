package com.blog.system.service.serviceImpl;

import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import com.blog.system.domain.vo.BlogOnerProfileVO;
import com.blog.system.mapper.UserProfileMapper;
import com.blog.system.service.UserProfileService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *  前台用户信息处理
 *
 * @author 31373
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImpl(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public BlogOnerProfileVO getBlogOwnerProfile() {
        BlogOnerProfileVO blogOwnerProfile = userProfileMapper.getBlogOwnerProfile();
        //加密adminId和解析ip地址
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(blogOwnerProfile.getAdminId().toString());
        blogOwnerProfile.setAdminId(encode);
        String realAddressByIP = AddressUtils.getRealAddressByIP(blogOwnerProfile.getLastLoginIp());
        blogOwnerProfile.setLastLoginIp(realAddressByIP);
        return blogOwnerProfile;
    }
}
