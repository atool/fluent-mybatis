package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.condition.interfaces.IEntityUpdate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static cn.org.atool.fluent.mybatis.mapper.MapperConst.WRAPPER;

/**
 * @ClassName IEntityMapper
 * @Description 实例Mapper基类
 * @Author darui.wu
 * @Date 2019-06-25 14:00
 */
public interface IEntityMapper<T> extends BaseMapper<T>, IMapper {
    /**
     * 批量插入数据，实例的主键必须已经赋值好
     *
     * @param list
     * @return
     */
    int insertBatch(List<T> list);

    /**
     * 根据update对象更新记录
     *
     * @param update
     * @return
     */
    int updateBy(@Param(WRAPPER) IEntityUpdate update);
}