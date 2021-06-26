package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.GetterFunc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * MappingKits
 *
 * @author wudarui
 */
public class MappingKits {
    public static <E extends IEntity> List<String> toColumns(Class<E> klass, GetterFunc<E> getter, GetterFunc<E>... getters) {
        List<String> list = new ArrayList<>(getters.length + 1);
        list.add(toColumn(klass, getter));
        for (GetterFunc func : getters) {
            list.add(toColumn(klass, func));
        }
        return list;
    }


    public static List<String> toColumns(FieldMapping column, FieldMapping... excludes) {
        List<String> list = new ArrayList<>(excludes.length + 1);
        list.add(column.column);
        Stream.of(excludes).forEach(c -> list.add(c.column));
        return list;
    }

    /**
     * 根据getter函数返回数据库字段名称
     *
     * @param klass IEntity类
     * @param func  getter函数
     * @param <E>   IEntity类
     * @return 数据库字段名称
     */
    public static <E extends IEntity> String toColumn(Class<E> klass, GetterFunc<E> func) {
        String field = LambdaUtil.resolve(func);
        String column = IRefs.instance().findColumnByField(klass, field);
        return column;
    }
}
