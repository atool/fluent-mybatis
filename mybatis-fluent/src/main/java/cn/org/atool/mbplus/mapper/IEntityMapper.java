package cn.org.atool.mbplus.mapper;

import cn.org.atool.mbplus.base.IEntityUpdate;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 插入单条记录（主键必须赋值好）
     *
     * @param entity
     * @return
     */
    int insertWithPk(T entity);

    /**
     * 根据update对象更新记录
     *
     * @param update
     * @return
     */
    int updateBy(@Param(Constants.WRAPPER) IEntityUpdate update);
}
