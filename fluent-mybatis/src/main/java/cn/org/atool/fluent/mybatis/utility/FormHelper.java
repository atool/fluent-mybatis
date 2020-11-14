package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.FormItem;
import cn.org.atool.fluent.mybatis.model.IFormQuery;
import cn.org.atool.fluent.mybatis.model.IPagedList;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * Form操作辅助类
 *
 * @author wudarui
 */
public class FormHelper {

    public static <E extends IEntity> IPagedList<E> paged(IFormQuery query) {
        return IRefs.instance().findMapper(query.entityClass()).stdPagedEntity(query);
    }

    /**
     * 按条件分页查询
     *
     * @param clazz
     * @param condition
     * @param <E>
     * @return
     */
    public static <E extends IEntity> IPagedList<E> paged(Class<E> clazz, Form condition) {
        assertNotNull("clazz", clazz);
        if (condition.getNextId() != null && condition.getCurrPage() != null) {
            throw new RuntimeException("nextId and currPage can only have one value");
        } else if (condition.getNextId() == null && condition.getCurrPage() == null) {
            throw new RuntimeException("nextId and currPage must have one value");
        } else {
            IQuery<E, ?> query = IRefs.instance().defaultQuery(clazz);
            WhereBase where = query.where();
            for (FormItem item : condition.getItems()) {
                String column = IRefs.instance().findColumnByField(clazz, item.getKey());
                if (isBlank(column)) {
                    throw new RuntimeException("the field[" + item.getKey() + "] of Entity[" + clazz.getSimpleName() + "] not found.");
                }
                where.and.apply(column, SqlOp.valueOf(item.getOp()), item.getValue());
            }
            if (condition.getCurrPage() != null) {
                int from = condition.getPageSize() * (condition.getCurrPage() - 1);
                query.limit(from, condition.getPageSize());
                return IRefs.instance().findMapper(clazz).stdPagedEntity(query);
            } else {
                String column = IRefs.instance().findPrimaryColumn(clazz);
                where.and.apply(column, SqlOp.GE, condition.getNextId());
                query.limit(condition.getPageSize());
                return IRefs.instance().findMapper(clazz).tagPagedEntity(query);
            }
        }
    }
}
