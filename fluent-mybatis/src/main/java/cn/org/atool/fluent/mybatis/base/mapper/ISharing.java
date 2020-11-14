package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * ISharingMapper: 分库分表操作
 *
 * @param <T>
 * @author darui.wu
 */
public interface ISharing<T> extends IMapper {
    /**
     * 根据IEntityUpdate修改记录
     *
     * @param update  更新信息
     * @param comment 分库
     * @param table   分表
     * @return
     */
    int updateSpecByQuery(@Param(Param_EW) IUpdate update,
                          @Param(SPEC_COMMENT) String comment,
                          @Param(SPEC_TABLE) String table
    );

    /**
     * 根据IEntityQuery删除记录
     *
     * @param query   查询条件
     * @param comment 分库
     * @param table   分表
     * @return
     */
    int deleteSpec(@Param(Param_EW) IQuery query,
                   @Param(SPEC_COMMENT) String comment,
                   @Param(SPEC_TABLE) String table
    );

    /**
     * 根据IEntityQuery查询记录
     *
     * @param query   查询条件
     * @param comment 分库
     * @param table   分表
     * @return
     */
    List<T> selectSpecList(@Param(Param_EW) IQuery query,
                           @Param(SPEC_COMMENT) String comment,
                           @Param(SPEC_TABLE) String table
    );
}