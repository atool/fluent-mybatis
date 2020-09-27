package cn.org.atool.fluent.mybatis.customize;

import java.util.Date;

/**
 * IBaseEntity 应用通用接口
 *
 * @author:darui.wu Created by darui.wu on 2020/6/2.
 */
public interface IBaseEntity<T extends IBaseEntity> {
    Long getId();

    Date getGmtCreated();

    Date getGmtModified();

    Boolean getIsDeleted();

    T setId(Long id);

    T setGmtCreated(Date date);

    T setGmtModified(Date date);

    T setIsDeleted(Boolean isDeleted);
}