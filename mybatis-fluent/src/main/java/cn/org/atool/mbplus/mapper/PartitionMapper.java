package cn.org.atool.mbplus.mapper;

import cn.org.atool.mbplus.base.IEntityQuery;
import cn.org.atool.mbplus.base.IEntityUpdate;
import cn.org.atool.mbplus.util.PartitionConst;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PartitionMapper<T> extends IMapper {
    /**
     * 根据IEntityUpdate修改记录
     *
     * @param update 更新信息
     * @param pdb    分库
     * @param ptb    分表
     * @return
     */
    int updateInPartition(@Param(Constants.WRAPPER) IEntityUpdate update,
                          @Param(PartitionConst.PARTITION_DATABASE) String pdb,
                          @Param(PartitionConst.PARTITION_TABLE) String ptb
    );

    /**
     * 根据IEntityQuery删除记录
     *
     * @param query 查询条件
     * @param pdb   分库
     * @param ptb   分表
     * @return
     */
    int deleteInPartition(@Param(Constants.WRAPPER) IEntityQuery query,
                          @Param(PartitionConst.PARTITION_DATABASE) String pdb,
                          @Param(PartitionConst.PARTITION_TABLE) String ptb
    );

    /**
     * 根据IEntityQuery查询记录
     *
     * @param query 查询条件
     * @param pdb   分库
     * @param ptb   分表
     * @return
     */
    List<T> selectListInPartition(@Param(Constants.WRAPPER) IEntityQuery query,
                                  @Param(PartitionConst.PARTITION_DATABASE) String pdb,
                                  @Param(PartitionConst.PARTITION_TABLE) String ptb
    );
}
