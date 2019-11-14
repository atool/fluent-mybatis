package cn.org.atool.fluent.mybatis.demo.generate.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP.Column;
import com.baomidou.mybatisplus.annotation.*;
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
@Setter
@Getter
@Accessors(chain = true)
@TableName(NoPrimaryMP.Table_Name)
public class NoPrimaryEntity implements IEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableField(value = Column.column_1)
    private Integer column1;
    /**
     * 
     */
    @TableField(value = Column.column_2)
    private String column2;

    @Override
    public Serializable findPk() {
        return null;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return NoPrimaryEntityHelper.map(this);
    }
}
