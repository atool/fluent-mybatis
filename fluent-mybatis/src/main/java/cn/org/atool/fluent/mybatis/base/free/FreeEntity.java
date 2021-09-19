package cn.org.atool.fluent.mybatis.base.free;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;

/**
 * 空实体, 仅仅为拼接查询使用
 *
 * @author darui.wu
 */
final class FreeEntity extends RichEntity {
    @Override
    public Class<? extends IEntity> entityClass() {
        return FreeEntity.class;
    }
}