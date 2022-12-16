package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;

import java.io.Serializable;
import java.util.function.BiFunction;

@SuppressWarnings("rawtypes")
@FunctionalInterface
public interface QueryFunction extends BiFunction<IEntityMapper, IQuery, Object>, Serializable {
}
