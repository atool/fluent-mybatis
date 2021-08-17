package cn.org.atool.fluent.mybatis.db.oracle11.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.functions.TableSupplier;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * OracleUserEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"unchecked"})
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
    @TableField("CODE")
    private String code;

    /**
     *
     */
    @TableField("IS_DELETED")
    private Boolean isDeleted;

    /**
     *
     */
    @TableField("VERSION2")
    private Long version2;

    @Override
    public Serializable findPk() {
        return this.id;
    }

    @Override
    public final Class<? extends IEntity> entityClass() {
        return OracleUserEntity.class;
    }

    @Override
    public final OracleUserEntity changeTableBelongTo(TableSupplier supplier) {
        return super.changeTableBelongTo(supplier);
    }

    @Override
    public final OracleUserEntity changeTableBelongTo(String table) {
        return super.changeTableBelongTo(table);
    }
}