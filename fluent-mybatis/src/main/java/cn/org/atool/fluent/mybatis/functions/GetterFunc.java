package cn.org.atool.fluent.mybatis.functions;

import java.io.Serializable;
import java.util.function.Function;

public interface GetterFunc<T> extends Serializable, Function<T, Object> {
}