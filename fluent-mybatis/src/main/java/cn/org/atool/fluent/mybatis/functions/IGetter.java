package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.utility.LambdaUtil;
import lombok.NonNull;

import java.io.Serializable;

/**
 * IEntity::getter()函数
 *
 * @author wudarui
 */
@FunctionalInterface
public interface IGetter<E> extends Serializable, Comparable<String> {
    /**
     * 返回属性值
     *
     * @param entity Entity
     * @return 属性值
     */
    Object get(E entity);

    @Override
    default int compareTo(@NonNull String o) {
        String fieldName = LambdaUtil.resolve(this);
        return fieldName.compareTo(o);
    }
}
