package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.annotation.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.model.SharedString;
import cn.org.atool.fluent.mybatis.interfaces.IWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.ORDER_BY;
import static cn.org.atool.fluent.mybatis.interfaces.model.SqlOp.RETAIN;
import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;
import static java.util.stream.Collectors.joining;

/**
 * BaseOrder: 排序对象基类
 *
 * @param <O> 排序对象
 * @author darui.wu
 */
public abstract class OrderByBase<
    O extends OrderByBase<O, W>,
    W extends IWrapper<?, W, ?>
    >
    extends BaseSegment<O, W> {
    /**
     * 排序字段
     */
    private final List<SharedString> apply = new ArrayList<>();

    private SharedString last = null;

    protected OrderByBase(W wrapper) {
        super(wrapper);
    }

    /**
     * 按升序排：ORDER BY 字段, ... ASC
     * <p>例: asc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O asc(String... columns) {
        if (isNotEmpty(columns)) {
            this.apply(Stream.of(columns).map(column -> column + SPACE + ASC).collect(joining(COMMA_SPACE)));
        } else if (last != null) {
            last.append(SPACE + ASC);
        }
        return (O) this;
    }

    /**
     * 按降序排：ORDER BY 字段, ... DESC
     * <p>例: desc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O desc(String... columns) {
        if (isNotEmpty(columns)) {
            this.apply(Stream.of(columns).map(column -> column + SPACE + DESC).collect(joining(COMMA_SPACE)));
        } else {
            last.append(SPACE + DESC);
        }
        return (O) this;
    }

    /**
     * 增加字段
     *
     * @param column 字段
     * @return self
     */
    private O apply(String column) {
        if (isNotEmpty(column)) {
            this.last = SharedString.str(column);
            this.apply.add(this.last);
        }
        return (O) this;
    }

    @Override
    public O set(FieldMeta field) {
        return this.apply(field.column);
    }

    /**
     * order by设置
     *
     * @return 查询器Query
     */
    @Override
    public W end() {
        String segment = this.apply.stream().map(SharedString::toString).collect(joining(COMMA_SPACE));
        this.wrapper.getWrapperData().apply(ORDER_BY, EMPTY, segment, RETAIN);
        return super.end();
    }
}