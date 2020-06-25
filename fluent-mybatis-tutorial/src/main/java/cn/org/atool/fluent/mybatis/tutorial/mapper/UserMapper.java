package cn.org.atool.fluent.mybatis.tutorial.mapper;

import cn.org.atool.fluent.mybatis.tutorial.entity.UserEntity;
import cn.org.atool.fluent.mybatis.base.IEntityMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * user Mapper接口
 * </p>
 *
 * @author generate code
 */
@Mapper
public interface UserMapper extends IEntityMapper<UserEntity>{
}