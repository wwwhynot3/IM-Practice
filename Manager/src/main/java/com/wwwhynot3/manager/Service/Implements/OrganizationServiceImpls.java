package com.wwwhynot3.manager.Service.Implements;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wwwhynot3.manager.Entity.Organization;
import com.wwwhynot3.manager.Entity.User;
import com.wwwhynot3.manager.Mapper.OrganizationMapper;
import com.wwwhynot3.manager.Mapper.UserMapper;
import com.wwwhynot3.manager.Service.Interfaces.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizationServiceImpls implements OrganizationService {
    private final OrganizationMapper organizationMapper;
    private final UserMapper userMapper;

    @Override
    public boolean createOrganization(Organization organization) {
        return organizationMapper.insert(organization) > 0;
    }

    @Override
    public boolean modifyOrganization(Organization organization) {
        return organizationMapper.updateById(organization) > 0;
    }

    @Override
    public Organization getOrganization(Long organizationId) {
        return organizationMapper.selectById(organizationId);
    }

    @Override
    public boolean deleteOrganization(Long organizationId) {
        return organizationMapper.deleteById(organizationId) > 0;
    }

    @Override
    public int addUsersToOrganization(Long organizationId, Long[] userIds) {
        return userIds.length == 0 ? 0 : userMapper.update(Wrappers.<User>lambdaUpdate()
                .in(User::getUserId, (Object[]) userIds)
                .set(User::getOrganizationId, organizationId));
    }

    @Override
    public int removeUsersFromOrganization(Long[] userIds) {
        return userIds.length == 0 ? 0 : userMapper
                .delete(Wrappers.lambdaQuery(User.class)
                        .in(User::getUserId, (Object[]) userIds));
    }

    @Override
    public int modifyUsersFromOrganization(Long organizationId, Long[] userIds) {
        return userIds.length == 0 ? 0 : userMapper.update(Wrappers.<User>lambdaUpdate()
                .in(User::getUserId, (Object[]) userIds)
                .set(User::getOrganizationId, organizationId));

    }

    @Override
    public List<Organization> getFullOrganization(Long organizationId) {
        List<Organization> res = new ArrayList<>();
        Organization root = organizationMapper.selectById(organizationId);
        while (root != null && !root.getOrganizationId().equals(root.getOrganizationIdParent())) {
            res.add(root);
            root = organizationMapper.selectById(root.getOrganizationIdParent());
        }
        return res;
    }
}
