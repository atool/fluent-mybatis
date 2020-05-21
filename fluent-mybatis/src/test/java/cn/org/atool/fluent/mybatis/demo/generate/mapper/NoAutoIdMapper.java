package cn.org.atool.fluent.mybatis.demo.generate.mapper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.mapper.IEntityMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper接口
 * </p>
 *
 * @author generate code
 */
@Mapper
@Component("newNoAutoIdMapper")
public interface NoAutoIdMapper extends IEntityMapper<NoAutoIdEntity>{
}