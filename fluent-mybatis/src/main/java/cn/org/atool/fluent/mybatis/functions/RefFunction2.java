package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.EntityRefKit;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * RefFunction2: 分组关联
 *
 * @param <E>
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
@AllArgsConstructor
public class RefFunction2<E> implements RefFunction<List<E>> {
    private final Class entityClass;

    private final String refMethodName;

    public final RefFunction<List<E>> function;

    @Override
    public Object apply(List<E> entities) {
        return function.apply(entities);
    }

    public void relation(Object source) {
        RefKey refKey = RefKit.byEntity(entityClass).findRefKey(refMethodName);
        List entities = new ArrayList();
        if (source instanceof List) {
            entities.addAll((List) source);
        } else {
            entities.add(source);
        }
        if (!entities.isEmpty()) {
            List refs = (List) function.apply(entities);
            EntityRefKit.groupRelation(refKey, entities, refs);
        }
    }
}