package cn.org.atool.fluent.mybatis.generator.shared5.entity;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * IdcardEntity: 数据映射实体定义
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
    table = "idcard",
    schema = "fluent_mybatis",
    useCached = true
)
public class IdcardEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(
        value = "id",
        auto = false,
        seqName = "SELECT NEXTVAL('testSeq')",
        before = true
    )
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