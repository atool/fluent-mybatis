package cn.org.atool.mybatis.fluent.demo.mapper;

import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.mapper.IEntityMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper接口
 * </p>
 *
 * @author generate code
 */
@Mapper
public interface UserMapper extends IEntityMapper<UserEntity> {
}
