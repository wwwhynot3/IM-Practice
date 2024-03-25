package com.wwwhynot3.manager.Mapper;

import com.github.yulichang.base.MPJBaseMapper;

import java.util.Collection;

public interface BatchInsertionBaseMapper<T> extends MPJBaseMapper<T> {
    /**
     * 批量插入 仅适用于MySQL
     *
     * @param entityList 需要插入的实体集合数量大于1
     * @return 影响的行数
     */

    int insertBatchSomeColumn(Collection<T> entityList);
}
