package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * IWrapper空实现
 *
 * @author darui.wu
 */
public class EmptyWrapper implements IWrapper {
    public static final EmptyWrapper INSTANCE = new EmptyWrapper();

    private EmptyWrapper() {
    }

    @Override
    public WhereBase where() {
        throw new RuntimeException("not support.");
    }

    @Override
    public WrapperData getWrapperData() {
        throw new RuntimeException("not support.");
    }

    @Override
    public Supplier<String> getTable() {
        throw new RuntimeException("not support.");
    }

    @Override
    public Optional<IMapping> mapping() {
        throw new RuntimeException("not support.");
    }

    @Override
    public DbType dbType() {
        throw new RuntimeException("not support.");
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("not support.");
    }
}