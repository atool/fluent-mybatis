package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.AllArgsConstructor;

/**
 * 实体关联关系key值构造
 *
 * @param <S>
 * @param <D>
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
@AllArgsConstructor
public class RefKey<S extends IEntity, D extends IEntity> {
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
     * 构造关联关系
     */
    public static <S extends IEntity, D extends IEntity>
    RefKey<S, D> refKey(String refName, boolean isList, RefKeyFunc<S> src, RefKeyFunc<D> ref) {
        return new RefKey(refName, src, ref, isList);
    }
}