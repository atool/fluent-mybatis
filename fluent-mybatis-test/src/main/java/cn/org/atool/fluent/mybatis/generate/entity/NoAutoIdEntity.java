package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.TableName;
import cn.org.atool.fluent.mybatis.base.IEntity;


import cn.org.atool.fluent.mybatis.generate.helper.NoAutoIdEntityHelper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate code
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("no_auto_id")
public class NoAutoIdEntity implements IEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id", auto = false, seqName="test")
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

    @Override
    public Map<String, Object> toMap() {
        return NoAutoIdEntityHelper.map(this);
    }

    @Override
    public Map<String, Object> columnMap() {
        return NoAutoIdEntityHelper.columnMap(this);
    }
}