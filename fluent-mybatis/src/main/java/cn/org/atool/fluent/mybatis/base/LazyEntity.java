package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.annotation.NotField;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Entity基类处理
 *
 * @author darui.wu
 */
public abstract class LazyEntity {

    @NotField
    protected transient Set<String> loaded = new HashSet<>(4);

    /**
     * 加载关联信息
     *
     * @param relation 关联点
     * @param set      set方法
     * @param <T>
     */
    protected <T> void lazyLoad(String relation, Consumer<T> set) {
        if (loaded.contains(relation)) {
            return;
        }
        T result = EntityLazyQuery.finder().load(relation, this);
        set.accept(result);
        loaded.add(relation);
    }
}