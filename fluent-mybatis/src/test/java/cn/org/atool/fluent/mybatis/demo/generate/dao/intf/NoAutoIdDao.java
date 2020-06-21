package cn.org.atool.fluent.mybatis.demo.generate.dao.intf;

import cn.org.atool.fluent.mybatis.interfaces.IDao;
import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName NoAutoIdDao
 * @Description NoAutoIdEntity数据操作接口
 *
 * @author generate code
 */
@Repository
public interface NoAutoIdDao extends IDao<NoAutoIdEntity>  {
}