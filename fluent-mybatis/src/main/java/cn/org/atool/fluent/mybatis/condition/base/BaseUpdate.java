package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.model.PagedOffset;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.COMMA;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * AbstractUpdateWrapper
 *
 * @param <E> 对应的实体类
 * @param <U> 更新器
 * @param <Q> 对应的查询器
 * @author darui.wu
 * @date 2020/6/17 4:24 下午
 */
public abstract class BaseUpdate<E extends IEntity, U, Q extends IQuery<E, Q>>
    extends Wrapper<E, U, Q> implements IUpdate<E, U, Q> {
    private static final long serialVersionUID = 6181348549200073762L;
    /**
     * SQL 更新字段内容，例如：name='1',age=2
     */
    private final List<String> sqlSet;
    /**
     * 按map更新
     */
    @Getter
    private final Map<String, Object> updates = new HashMap<>();

    protected BaseUpdate(Class entityClass) {
        super(entityClass);
        this.sqlSet = new ArrayList<>();
    }

    @Override
    public U set(String column, Object value) {
        super.validateColumn(column);
        this.updates.put(column, value);
        return (U) this;
    }

    @Override
    public U setSql(String column, String functionSql, Object... values) {
        super.validateColumn(column);
        if (isNotEmpty(functionSql)) {
            sqlSet.add(column + " = " + super.parameters.paramSql(functionSql, values));
        }
        return (U) this;
    }

    @Override
    public U limit(int limit) {
        super.paged = new PagedOffset(0, limit);
        return (U) this;
    }

    /**
     * 获取 更新 SQL 的 SET 片段
     * 被 xml 文件引用
     *
     * @return sql
     */
    public String getSqlSet() {
        if (isEmpty(sqlSet)) {
            return null;
        } else {
            return String.join(COMMA, sqlSet);
        }
    }
}