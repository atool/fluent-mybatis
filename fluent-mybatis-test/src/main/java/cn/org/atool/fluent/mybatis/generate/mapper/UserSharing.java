package cn.org.atool.fluent.mybatis.generate.mapper;

import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.base.ISharing;

import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  分库分表Mapper接口
 * </p>
 *
 * @author generate code
 */
@Mapper
public interface UserSharing extends ISharing<UserEntity>{
}