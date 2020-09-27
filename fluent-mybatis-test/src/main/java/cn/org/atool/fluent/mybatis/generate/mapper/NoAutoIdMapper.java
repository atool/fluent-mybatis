package cn.org.atool.fluent.mybatis.generate.mapper;

import cn.org.atool.fluent.mybatis.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.base.IEntityMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * no_auto_id Mapper接口
 * </p>
 *
 * @author generate code
 */
@Mapper
@Component("newNoAutoIdMapper")
public interface NoAutoIdMapper extends IEntityMapper<NoAutoIdEntity>{
}