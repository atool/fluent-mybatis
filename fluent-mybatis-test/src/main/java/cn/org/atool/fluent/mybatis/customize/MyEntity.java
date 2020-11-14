package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.util.Date;

/**
 * IBaseEntity 应用通用接口
 *
 * @author:darui.wu Created by darui.wu on 2020/6/2.
 */
public interface MyEntity<E extends IEntity> {
    String getEnv();

    Long getTenant();

    Long getId();

    Date getGmtCreated();

    Date getGmtModified();

    Boolean getIsDeleted();

    E setEnv(String env);

    E setTenant(Long tenantId);

    E setId(Long id);

    E setGmtCreated(Date date);

    E setGmtModified(Date date);

    E setIsDeleted(Boolean isDeleted);
}