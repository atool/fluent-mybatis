package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import java.lang.String;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Powered by FluentMyBatis
 */
@Getter
@Setter
@Accessors(chain = true)
@FluentMybatis(table = "no_auto_id", mapperBeanPrefix = "new")
public class NoAutoIdEntity implements IEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id", auto = false, seqName="SELECT LAST_INSERT_ID() AS ID")
    private String id;
    /**
     * 
     */
    @TableField(value = "column_1")
    private String column1;

    @Override
    public Serializable findPk() {
        return id;
    }
}