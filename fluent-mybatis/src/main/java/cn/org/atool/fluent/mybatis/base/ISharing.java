package cn.org.atool.fluent.mybatis.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import static cn.org.atool.fluent.mybatis.test.method.model.XmlConstant.*;

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
    int updateSpecByQuery(@Param(WRAPPER) IUpdate update,
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
    int deleteSpec(@Param(WRAPPER) IQuery query,
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
    List<T> selectSpecList(@Param(WRAPPER) IQuery query,
                           @Param(SPEC_COMMENT) String comment,
                           @Param(SPEC_TABLE) String table
    );
}