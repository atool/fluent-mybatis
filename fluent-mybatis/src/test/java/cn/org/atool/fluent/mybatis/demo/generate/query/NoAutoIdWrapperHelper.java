package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.apply.*;
import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP.Property;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP.Column;


/**
 * <p>
 * NoAutoIdWrapperHelper
 * NoAutoIdEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class NoAutoIdWrapperHelper {
    /**
     * select字段设置
     */
    public static final class Selector extends BaseSelector<Selector>{
        public final ColumnSelector<Selector> id = new ColumnSelector<>(this, Column.id);
        public final ColumnSelector<Selector> column1 = new ColumnSelector<>(this, Column.column_1);

        Selector(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     * @param <W> 更新器或查询器
     */
    public static final class WrapperWhere<W extends Wrapper<NoAutoIdEntity, W, NoAutoIdQuery>>
        extends BaseWhere<NoAutoIdEntity, W, NoAutoIdQuery> {
        public final WhereString<NoAutoIdEntity, W, NoAutoIdQuery> id = new WhereString<>(this, Column.id, Property.id);
        public final WhereString<NoAutoIdEntity, W, NoAutoIdQuery> column1 = new WhereString<>(this, Column.column_1, Property.column1);

        WrapperWhere(W wrapper) {
            this(wrapper, true);
        }
        WrapperWhere(W wrapper, boolean and) {
            super(wrapper, and);
        }
    }

    /**
     * 分组设置
     */
    public static final class QueryGroup extends BaseGroup<QueryGroup> {
        public final GroupBy<QueryGroup> id = new GroupBy<>(this, Column.id);
        public final GroupBy<QueryGroup> column1 = new GroupBy<>(this, Column.column_1);

        QueryGroup(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends BaseHaving<Having> {
        public final HavingBy<Having> id = new HavingBy<>(this, Column.id);
        public final HavingBy<Having> column1 = new HavingBy<>(this, Column.column_1);

        Having(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class QueryOrder extends BaseOrder<QueryOrder> {
        public final OrderBy<QueryOrder> id = new OrderBy<>(this, Column.id);
        public final OrderBy<QueryOrder> column1 = new OrderBy<>(this, Column.column_1);

        QueryOrder(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends BaseSetter<NoAutoIdEntity, NoAutoIdUpdate> {
        public final SetString<NoAutoIdUpdate> id = new SetString<>(this, Column.id, Property.id);
        public final SetString<NoAutoIdUpdate> column1 = new SetString<>(this, Column.column_1, Property.column1);

        UpdateSetter(NoAutoIdUpdate update) {
            super(update);
        }
    }
}