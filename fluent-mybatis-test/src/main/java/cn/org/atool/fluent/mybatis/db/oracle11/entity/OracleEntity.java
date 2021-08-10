package cn.org.atool.fluent.mybatis.db.oracle11.entity;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.generate.entity.IdcardEntity;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
    @TableId("id")
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
    public Serializable findPk() {
        return this.id;
    }

    @Override
    public final Class<? extends IEntity> entityClass() {
        return IdcardEntity.class;
    }
}