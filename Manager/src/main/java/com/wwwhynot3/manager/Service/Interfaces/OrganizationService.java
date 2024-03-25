package com.wwwhynot3.manager.Service.Interfaces;

import com.wwwhynot3.manager.Entity.Organization;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrganizationService {
    boolean createOrganization(Organization organization);

    boolean modifyOrganization(Organization organization);

    Organization getOrganization(Long organizationId);

    boolean deleteOrganization(Long organizationId);

    int addUsersToOrganization(Long organizationId, Long[] userIds);

    int removeUsersFromOrganization(Long[] userIds);

    int modifyUsersFromOrganization(Long organizationId, Long[] userIds);

    List<Organization> getFullOrganization(Long organizationId);
}
