package cn.org.atool.fluent.mybatis.demo.generate.mapper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.mapper.PartitionMapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  分库分表Mapper接口
 * </p>
 *
 * @author generate code
 */
@Mapper
public interface UserPartitionMapper extends PartitionMapper<UserEntity>{
}