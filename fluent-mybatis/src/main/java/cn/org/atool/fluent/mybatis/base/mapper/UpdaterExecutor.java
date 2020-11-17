package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.base.crud.IUpdate;

public class UpdaterExecutor {
    private final IRichMapper mapper;

    private final IUpdate updater;

    public UpdaterExecutor(IRichMapper mapper, IUpdate updater) {
        this.mapper = mapper;
        this.updater = updater;
    }

    public int updateBy() {
        return this.mapper.updateBy(this.updater);
    }
}