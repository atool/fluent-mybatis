package com.baomidou.mybatisplus.core.conditions;

import cn.org.atool.fluent.mybatis.condition.base.PredicateField;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;

import java.util.function.Predicate;

/**
 * AbstractWrapper
 *
 * @author darui.wu
 * @create 2020/6/3 11:50 上午
 */
@Deprecated
public abstract class AbstractWrapper<T, R, Children extends AbstractWrapper<T, R, Children>>
    extends cn.org.atool.fluent.mybatis.condition.base.AbstractWrapper<T, R, Children> {

    public Children select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        throw new RuntimeException("not implements");
    }

    public Children select(Predicate<TableFieldInfo> predicate) {
        throw new RuntimeException("not implement");
    }

    /**
     * Query
     *
     * @param predicate
     * @return
     */
    public Children select(PredicateField predicate) {
        Predicate<TableFieldInfo> predicate1 = new Predicate<TableFieldInfo>() {
            @Override
            public boolean test(TableFieldInfo fieldInfo) {
                return predicate.test(fieldInfo);
            }
        };
        return select(predicate1);
    }
}