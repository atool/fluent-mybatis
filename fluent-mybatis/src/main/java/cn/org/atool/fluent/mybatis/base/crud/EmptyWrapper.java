package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.List;
import java.util.Optional;

/**
 * IWrapper空实现
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public class EmptyWrapper implements IWrapper {
    public static final EmptyWrapper INSTANCE = new EmptyWrapper();

    private EmptyWrapper() {
    }

    @Override
    public WhereBase where() {
        throw new RuntimeException("not support.");
    }

    @Override
    public WrapperData data() {
        throw new RuntimeException("not support.");
    }

    @Override
    public IFragment table(boolean notFoundError) {
        throw new RuntimeException("not support.");
    }

    @Override
    public Optional<IMapping> mapping() {
        throw new RuntimeException("not support.");
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("not support.");
    }
}