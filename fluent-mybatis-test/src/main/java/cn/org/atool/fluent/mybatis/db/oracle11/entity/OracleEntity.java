package cn.org.atool.fluent.mybatis.db.oracle11.entity;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.generator.shared5.entity.IdcardEntity;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "oracle_table",
    dbType = DbType.ORACLE
)
public class OracleEntity extends RichEntity {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 是否逻辑删除
     */
    @TableField(
        value = "is_deleted",
        insert = "0"
    )
    @LogicDelete
    private Long isDeleted;

    /**
     *
     */
    @TableField("code")
    private String code;

    /**
     * 版本锁
     */
    @TableField(
        value = "version",
        insert = "0",
        update = "`version` + 1"
    )
    @Version
    private Long version;

    @Override
    public final Class entityClass() {
        return IdcardEntity.class;
    }
}