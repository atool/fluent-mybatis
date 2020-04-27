package cn.org.atool.fluent.mybatis.demo.generate.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoAutoIdEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP.Column;
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
@Getter
@Setter
@Accessors(chain = true)
@TableName(NoAutoIdMP.Table_Name)
public class NoAutoIdEntity implements IEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableField(value = Column.column_1)
    private String column1;
    /**
     * 
     */
    @TableId(value = Column.id)
    private String id;

    @Override
    public Serializable findPk() {
        return this.id;
    }

    /**
     * 将实体对象转换为map
     */
    @Override
    public Map<String, Object> toMap() {
        return NoAutoIdEntityHelper.map(this);
    }
}
