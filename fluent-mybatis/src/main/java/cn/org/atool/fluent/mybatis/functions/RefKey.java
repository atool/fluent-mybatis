package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.base.intf.IRelation;
import lombok.AllArgsConstructor;

/**
 * 实体关联关系key值构造
 *
 * @param <S>
 * @param <D>
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AllArgsConstructor
public class RefKey<S, D> {
    /**
     * 关联方法名称
     */
    public final String refMethodName;
    /**
     * 原实例关联键值构造
     */
    public final RefKeyFunc<S> src;
    /**
     * 关联实例关联键值构造
     */
    public final RefKeyFunc<D> ref;
    /**
     * 是否 1:N关系
     */
    public final boolean isList;
    /**
     * Entity Ref {@link RefMethod}在 {@link IRelation}中的实现方法
     */
    public final IGetter finder;

    /**
     * 构造关联关系
     */
    public static <S, D> RefKey<S, D> refKey(String refName, boolean isList,
                                             RefKeyFunc<S> src, RefKeyFunc<D> ref,
                                             IGetter finder) {
        return new RefKey(refName, src, ref, isList, finder);
    }
}