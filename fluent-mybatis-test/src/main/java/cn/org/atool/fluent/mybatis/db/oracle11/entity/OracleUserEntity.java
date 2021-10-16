package cn.org.atool.fluent.mybatis.db.oracle11.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * OracleUserEntity: 数据映射实体定义
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
    table = "TEST_USER",
    schema = "SYSTEM",
    dbType = DbType.ORACLE
)
public class OracleUserEntity extends RichEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(
        value = "ID",
        auto = false,
        seqName = "SELECT TEST_USER_SEQ.nextval AS ID FROM DUAL",
        before = true
    )
    private Long id;

    /**
     *
     */
    @TableField(
        value = "IS_DELETED",
        insert = "0"
    )
    @LogicDelete
    private Boolean isDeleted;

    /**
     *
     */
    @TableField("CODE")
    private String code;

    /**
     *
     */
    @TableField("VERSION2")
    private Long version2;

    @Override
    public final Class entityClass() {
        return OracleUserEntity.class;
    }
}