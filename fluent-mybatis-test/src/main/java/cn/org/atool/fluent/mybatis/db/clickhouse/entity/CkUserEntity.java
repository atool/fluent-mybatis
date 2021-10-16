package cn.org.atool.fluent.mybatis.db.clickhouse.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.typehandler.ClickArrayHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Array;
import java.util.Date;

/**
 * CkUserEntity: 数据映射实体定义
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
    table = "user",
    dbType = DbType.CLICK_HOUSE
)
public class CkUserEntity extends RichEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableField("createTime")
    private Date createTime;

    /**
     *
     */
    @TableField("id")
    private Integer id;

    /**
     *
     */
    @TableField("name")
    private String name;

    /**
     *
     */
    @TableField(value = "ranks", typeHandler = ClickArrayHandler.class)
    private Array ranks;

    /**
     *
     */
    @TableField("score")
    private Double score;

    /**
     *
     */
    @TableField("score2")
    private Double score2;

    /**
     *
     */
    @TableField("state")
    private Integer state;

    @Override
    public final Class entityClass() {
        return CkUserEntity.class;
    }
}