package com.wwwhynot3.manager.Config.MybatisPlus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

public class InsertIgnoreBatchSqlInjector extends DefaultSqlInjector {
    public InsertIgnoreBatchSqlInjector() {
        super();
    }

    /**
     * 只有MySQL可以支持批量插入和insert ignore
     *
     * @param mapperClass
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertIgnoreBatchSomeColumn());
        return methodList;
    }
}
