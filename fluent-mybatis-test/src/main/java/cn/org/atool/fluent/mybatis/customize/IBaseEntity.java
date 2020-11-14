package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.entity.IEntity;

import java.util.Date;

/**
 * IBaseEntity 应用通用接口
 *
 * @author:darui.wu Created by darui.wu on 2020/6/2.
 */
public interface IBaseEntity<T extends IEntity> {
    String getEnv();

    Long getTenant();

    Long getId();

    Date getGmtCreated();

    Date getGmtModified();

    Boolean getIsDeleted();

    T setEnv(String env);

    T setTenant(Long tenantId);

    T setId(Long id);

    T setGmtCreated(Date date);

    T setGmtModified(Date date);

    T setIsDeleted(Boolean isDeleted);
}