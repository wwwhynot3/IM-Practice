package com.wwwhynot3.manager.Config.MybatisPlus;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.function.Predicate;


/**
 * @author huanglb
 * @version 0.1
 * @Description only works in MySQL
 */
public class InsertIgnoreBatchSomeColumn extends AbstractMethod {
    private Predicate<TableFieldInfo> predicate;

    public InsertIgnoreBatchSomeColumn() {
        super("insertBatchSomeColumn");
    }

    public InsertIgnoreBatchSomeColumn(Predicate<TableFieldInfo> predicate) {
        super("insertBatchSomeColumn");
        this.predicate = predicate;
    }

    public InsertIgnoreBatchSomeColumn(String name, Predicate<TableFieldInfo> predicate) {
        super(name);
        this.predicate = predicate;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
//        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
        /**
         * insert ignore sql template
         */
        String sqlTemplate = "<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>";
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        String insertSqlColumn = tableInfo.getKeyInsertSqlColumn(true, (String) null, false) + this.filterTableFieldInfo(fieldList, this.predicate, TableFieldInfo::getInsertSqlColumn, "");
        String columnScript = "(" + insertSqlColumn.substring(0, insertSqlColumn.length() - 1) + ")";
        String insertSqlProperty = tableInfo.getKeyInsertSqlProperty(true, "et.", false) + this.filterTableFieldInfo(fieldList, this.predicate, (i) -> {
            return i.getInsertSqlProperty("et.");
        }, "");
        insertSqlProperty = "(" + insertSqlProperty.substring(0, insertSqlProperty.length() - 1) + ")";
        String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, "list", (String) null, "et", ",");
        String keyProperty = null;
        String keyColumn = null;
        if (tableInfo.havePK()) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = SqlInjectionUtils.removeEscapeCharacter(tableInfo.getKeyColumn());
            } else if (null != tableInfo.getKeySequence()) {
                keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, this.builderAssistant);
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            }
        }

        String sql = String.format(sqlTemplate, tableInfo.getTableName(), columnScript, valuesScript);
        SqlSource sqlSource = super.createSqlSource(this.configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, (KeyGenerator) keyGenerator, keyProperty, keyColumn);
    }

    public InsertIgnoreBatchSomeColumn setPredicate(final Predicate<TableFieldInfo> predicate) {
        this.predicate = predicate;
        return this;
    }
}
