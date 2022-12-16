package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;

@SuppressWarnings({"unchecked", "rawtypes"})
public class WrapperPrinter {
    private final IMapping mapping;

    private final Class<? extends IWrapperMapper> mapperClass;

    public WrapperPrinter(IWrapper wrapper) {
        this.mapping = (IMapping) wrapper.mapping().orElseGet(() -> {
            String err = "Mapping not found";
            throw new RuntimeException(err);
        });
        this.mapperClass = mapping.mapperClass();
    }
}
