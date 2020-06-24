package cn.org.atool.fluent.mybatis.base.model;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;

/**
 * MarkerList: 按marker方式进行的分页查询结果
 *
 * @param <E> 实体对象类
 * @author darui.wu
 * @create 2020/6/24 10:45 上午
 */
@Getter
public class MarkerList<E> {
    /**
     * 本次查询结果集
     */
    private List<E> data;
    /**
     * 比本次查询多查出的记录
     * next == null: 表示没有更多记录
     * next != null: 可以用来构造具体的marker值传递到下一个查询
     */
    private E next;

    public MarkerList() {
    }

    public MarkerList(List<E> list, E next) {
        this.data = list;
        this.next = next;
    }

    /**
     * 构造marker标识
     *
     * @param parser marker解析函数
     * @param <MK>   Marker类型, 通常是String或者Number值
     * @return next的marker值
     */
    public <MK> MK parseMarker(Function<E, MK> parser) {
        return next == null ? null : parser.apply(next);
    }
}