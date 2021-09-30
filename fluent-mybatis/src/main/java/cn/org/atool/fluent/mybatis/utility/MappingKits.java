package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.refs.IRef;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * MappingKits: Entity字段映射工具类
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MappingKits {
    public static <E extends IEntity> String[] toColumns(Class<E> klass, IGetter<E> getter, IGetter<E>... getters) {
        List<String> list = new ArrayList<>(getters.length + 1);
        list.add(toColumn(klass, getter));
        for (IGetter func : getters) {
            list.add(toColumn(klass, func));
        }
        return list.toArray(new String[0]);
    }

    public static String[] toColumns(FieldMapping column, FieldMapping... excludes) {
        List<String> list = new ArrayList<>(excludes.length + 1);
        list.add(column.column);
        Stream.of(excludes).forEach(c -> list.add(c.column));
        return list.toArray(new String[0]);
    }

    /**
     * 根据getter函数返回数据库字段名称
     *
     * @param klass IEntity类
     * @param func  getter函数
     * @param <E>   IEntity类
     * @return 数据库字段名称
     */
    public static <E extends IEntity> String toColumn(Class<E> klass, IGetter<E> func) {
        String field = LambdaUtil.resolve(func);
        return IRef.columnOfField(klass, field);
    }
}