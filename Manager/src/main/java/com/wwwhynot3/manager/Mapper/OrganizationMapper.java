package com.wwwhynot3.manager.Mapper;

import com.wwwhynot3.manager.Entity.Organization;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrganizationMapper extends BatchInsertionBaseMapper<Organization> {
}
