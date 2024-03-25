package com.wwwhynot3.manager.View;

import com.wwwhynot3.manager.Entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationView {
    private List<Organization> orderedOrganizations;
    private String organizationName;
}
