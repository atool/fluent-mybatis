package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.apply.*;
import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP.Property;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP.Column;


/**
 * <p>
 * NoPrimaryWrapperHelper
 * NoPrimaryEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class NoPrimaryWrapperHelper {
    /**
     * select字段设置
     */
    public static final class Selector extends BaseSelector<Selector>{
        public final ColumnSelector<Selector> column1 = new ColumnSelector<>(this, Column.column_1);
        public final ColumnSelector<Selector> column2 = new ColumnSelector<>(this, Column.column_2);

        Selector(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     * @param <W> 更新器或查询器
     */
    public static final class WrapperWhere<W extends Wrapper<NoPrimaryEntity, W, NoPrimaryQuery>>
        extends BaseWhere<NoPrimaryEntity, W, NoPrimaryQuery> {
        public final WhereObject<NoPrimaryEntity, Integer, W, NoPrimaryQuery> column1 = new WhereObject<>(this, Column.column_1, Property.column1);
        public final WhereString<NoPrimaryEntity, W, NoPrimaryQuery> column2 = new WhereString<>(this, Column.column_2, Property.column2);

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
        public final GroupBy<QueryGroup> column1 = new GroupBy<>(this, Column.column_1);
        public final GroupBy<QueryGroup> column2 = new GroupBy<>(this, Column.column_2);

        QueryGroup(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends BaseHaving<Having> {
        public final HavingBy<Having> column1 = new HavingBy<>(this, Column.column_1);
        public final HavingBy<Having> column2 = new HavingBy<>(this, Column.column_2);

        Having(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class QueryOrder extends BaseOrder<QueryOrder> {
        public final OrderBy<QueryOrder> column1 = new OrderBy<>(this, Column.column_1);
        public final OrderBy<QueryOrder> column2 = new OrderBy<>(this, Column.column_2);

        QueryOrder(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends BaseSetter<NoPrimaryEntity, NoPrimaryUpdate> {
        public final SetObject<Integer, NoPrimaryUpdate> column1 = new SetObject<>(this, Column.column_1, Property.column1);
        public final SetString<NoPrimaryUpdate> column2 = new SetString<>(this, Column.column_2, Property.column2);

        UpdateSetter(NoPrimaryUpdate update) {
            super(update);
        }
    }
}