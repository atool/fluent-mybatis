package cn.org.atool.fluent.mybatis.db.mssql.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * MsUserEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "my_user",
    schema = "my_test",
    dbType = DbType.SQL_SERVER2012
)
public class MsUserEntity extends RichEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId("id")
    private Long id;

    /**
     *
     */
    @TableField("user_name")
    private String userName;

    @Override
    public final Class entityClass() {
        return MsUserEntity.class;
    }
}