package com.blog.system.service.serviceImpl;

import com.blog.common.core.domain.entity.Administrators;
import com.blog.common.exception.ServiceException;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.MessageUtils;
import com.blog.common.utils.SecurityUtils;
import com.blog.system.domain.dto.Collaborators;
import com.blog.system.domain.vo.AdministratorsVO;
import com.blog.system.mapper.SysUserMapper;
import com.blog.system.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 *
 * @author 31373
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;

    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public Administrators selectUserByUserName(String username) {
        return sysUserMapper.selectUserByUserName(username);
    }

    @Override
    public List<AdministratorsVO> selectAdminList() {
        return sysUserMapper.selectAdminList();
    }

    @Override
    public int addCollaborators(Collaborators collaborators) {
        //从当前登录用户信息中获取id
        Long adminId = SecurityUtils.getLoginUserOnAdmin().getAdminId();
        if (!SecurityUtils.isAdmin(adminId)){
            throw new ServiceException(MessageUtils.message("no.create.permission"));
        }
        //检测用户名是否存在
        if (sysUserMapper.selectUserByUserName(collaborators.getUsername()) != null){
            throw new ServiceException(MessageUtils.message("username.exists"));
        }
        Administrators administrators = new Administrators();
        administrators.setUsername(collaborators.getUsername());
        administrators.setNickname(collaborators.getNickname());
        administrators.setEmail(collaborators.getEmail());
        administrators.setPassword(SecurityUtils.encryptPassword(collaborators.getPassword()));
        administrators.setDescription(collaborators.getDescription());
        administrators.setUserType("collaborators");
        administrators.setCreateBy(String.valueOf(adminId));
        administrators.setCreateTime(DateUtils.getNowDate());
        return sysUserMapper.addCollaborators(administrators);
    }

    @Override
    public int updateCollaborators(Collaborators collaborators) {
        Long adminId = SecurityUtils.getLoginUserOnAdmin().getAdminId();
        if (!SecurityUtils.isAdmin(adminId)){
            throw new ServiceException(MessageUtils.message("no.update.permission"));
        }
        Administrators administrators = new Administrators();
        administrators.setAdminId(collaborators.getAdminId());
        administrators.setNickname(collaborators.getNickname());
        administrators.setEmail(collaborators.getEmail());
        administrators.setDescription(collaborators.getDescription());
        administrators.setUpdateBy(String.valueOf(adminId));
        administrators.setUpdateTime(DateUtils.getNowDate());
        return sysUserMapper.updateCollaborators(administrators);
    }

    @Override
    public int deleteCollaborators(Long id) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUserOnAdmin().getAdminId())){
            throw new ServiceException(MessageUtils.message("no.delete.permission"));
        }
        return sysUserMapper.deleteCollaborators(id);
    }

    @Override
    public Collaborators getCollaborators(Long id) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUserOnAdmin().getAdminId())){
            throw new ServiceException(MessageUtils.message("no.view.permission"));
        }
        return sysUserMapper.getCollaboratorsById(id);
    }

    @Override
    public int resetPassword(Long adminId, String password) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUserOnAdmin().getAdminId())){
            throw new ServiceException(MessageUtils.message("no.view.permission"));
        }
        String encryptPassword = SecurityUtils.encryptPassword(password);
        return sysUserMapper.resetPassword(adminId, encryptPassword);
    }

    @Override
    public void updateAdminDetailed(String adminId, String loginTime, String ipaddr) {
        sysUserMapper.updateAdminDetailed(adminId,loginTime,ipaddr);
    }
}
