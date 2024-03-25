package com.wwwhynot3.manager.Mapper;

import com.wwwhynot3.manager.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BatchInsertionBaseMapper<User> {
}
