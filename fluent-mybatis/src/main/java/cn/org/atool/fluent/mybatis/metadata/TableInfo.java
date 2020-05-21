package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.util.ArrayUtils;
import cn.org.atool.fluent.mybatis.util.Constants;
import cn.org.atool.fluent.mybatis.util.SimpleAssert;
import cn.org.atool.fluent.mybatis.util.StringUtils;
import cn.org.atool.fluent.mybatis.annotation.IdType;
import cn.org.atool.fluent.mybatis.annotation.KeySequence;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.MybatisConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * 数据库表反射信息
 *
 * @author darui.wu
 */
@Data
@Setter(AccessLevel.PACKAGE)
@Accessors(chain = true)
public class TableInfo implements Constants {

    /**
     * 实体类型
     */
    private Class<?> entityType;
    /**
     * 表主键ID 类型
     */
    private IdType idType = IdType.NONE;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表映射结果集
     */
    private String resultMap;
    /**
     * 是否是需要自动生成的 resultMap
     */
    private boolean autoInitResultMap;
    /**
     * 是否是自动生成的 resultMap
     */
    private boolean initResultMap;
    /**
     * 主键是否有存在字段名与属性名关联
     * <p>true: 表示要进行 as</p>
     */
    private boolean keyRelated = false;
    /**
     * 表主键ID 字段名
     */
    private String keyColumn;
    /**
     * 表主键ID 属性名
     */
    private String keyProperty;
    /**
     * 表主键ID 属性类型
     */
    private Class<?> keyType;
    /**
     * 表主键ID Sequence
     */
    private KeySequence keySequence;
    /**
     * 表字段信息列表
     */
    private List<FieldInfo> fieldList;
    /**
     * 命名空间 (对应的 mapper 接口的全类名)
     */
    private String currentNamespace;
    /**
     * MybatisConfiguration 标记 (Configuration内存地址值)
     */
    @Getter
    private MybatisConfiguration configuration;

    /**
     * 缓存包含主键及字段的 sql select
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String allSqlSelect;
    /**
     * 缓存主键字段的 sql select
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String sqlSelect;

    public TableInfo(Class<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * 设置 Configuration
     */
    void setConfiguration(Configuration configuration) {
        SimpleAssert.notNull(configuration, "Error: You need Initialize MybatisConfiguration !");
        this.configuration = (MybatisConfiguration) configuration;
    }

    /**
     * 获取主键的 select sql 片段
     *
     * @return sql 片段
     */
    public String getKeySqlSelect() {
        if (sqlSelect != null) {
            return sqlSelect;
        }
        if (StringUtils.isNotEmpty(keyProperty)) {
            sqlSelect = keyColumn;
            if (keyRelated) {
                sqlSelect += (" AS " + keyProperty);
            }
        } else {
            sqlSelect = EMPTY;
        }
        return sqlSelect;
    }

    /**
     * 获取包含主键及字段的 select sql 片段
     *
     * @return sql 片段
     */
    public String getAllSqlSelect() {
        if (allSqlSelect == null) {
            allSqlSelect = chooseSelect(FieldInfo::isSelect);
        }
        return allSqlSelect;
    }

    /**
     * 获取需要进行查询的 select sql 片段
     *
     * @param predicate 过滤条件
     * @return sql 片段
     */
    public String chooseSelect(Predicate<FieldInfo> predicate) {
        String sqlSelect = getKeySqlSelect();
        String fieldsSqlSelect = fieldList.stream().filter(predicate)
            .map(FieldInfo::getSqlSelect).collect(joining(COMMA));
        if (StringUtils.isNotEmpty(sqlSelect) && StringUtils.isNotEmpty(fieldsSqlSelect)) {
            return sqlSelect + COMMA + fieldsSqlSelect;
        } else if (StringUtils.isNotEmpty(fieldsSqlSelect)) {
            return fieldsSqlSelect;
        }
        return sqlSelect;
    }


    /**
     * 自动构建 resultMap 并注入(如果条件符合的话)
     */
    void initResultMapIfNeed() {
        if (autoInitResultMap && null == resultMap) {
            String id = currentNamespace + DOT + FLUENT_MYBATIS + UNDERSCORE + entityType.getSimpleName();
            List<ResultMapping> resultMappings = new ArrayList<>();
            if (keyType != null) {
                ResultMapping idMapping = new ResultMapping.Builder(configuration, keyProperty, keyColumn, keyType)
                    .flags(Collections.singletonList(ResultFlag.ID)).build();
                resultMappings.add(idMapping);
            }
            if (ArrayUtils.isNotEmpty(fieldList)) {
                fieldList.forEach(i -> resultMappings.add(i.getResultMapping(configuration)));
            }
            ResultMap resultMap = new ResultMap.Builder(configuration, id, entityType, resultMappings).build();
            configuration.addResultMap(resultMap);
            this.resultMap = id;
        }
    }
}