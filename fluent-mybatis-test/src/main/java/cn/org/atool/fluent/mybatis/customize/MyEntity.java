package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.Getter;

import java.util.Date;

/**
 * IBaseEntity 应用通用接口
 *
 * @author darui.wu Created by darui.wu on 2020/6/2.
 */
@SuppressWarnings({"unchecked"})
@Getter
public abstract class MyEntity<E extends MyEntity<E>> extends RichEntity {
    @TableId(
        value = "id",
        desc = "主键id"
    )
    private Long id;

    @TableField(
        value = "env",
        desc = "数据隔离环境"
    )
    private String env;

    @TableField(
        value = "tenant",
        desc = "租户标识"
    )
    private Long tenant;

    @TableField(
        value = "gmt_created",
        insert = "now()",
        desc = "创建时间"
    )
    @GmtCreate
    private Date gmtCreated;

    @TableField(
        value = "gmt_modified",
        insert = "now()",
        update = "now()",
        desc = "更新时间"
    )
    @GmtModified
    private Date gmtModified;

    @TableField(
        value = "is_deleted",
        insert = "0",
        desc = "是否逻辑删除"
    )
    @LogicDelete
    private Boolean isDeleted;

    public E setEnv(String env) {
        this.env = env;
        return (E) this;
    }

    public E setTenant(Long tenant) {
        this.tenant = tenant;
        return (E) this;
    }

    public E setId(Long id) {
        this.id = id;
        return (E) this;
    }

    public E setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
        return (E) this;
    }

    public E setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return (E) this;
    }

    public E setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return (E) this;
    }
}