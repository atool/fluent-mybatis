package cn.org.atool.fluent.mybatis.generate.entity.dao.intf;

import cn.org.atool.fluent.mybatis.base.IDao;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserDao
 * @Description UserEntity数据操作接口
 *
 * @author generate code
 */
@Repository
public interface UserDao extends IDao<UserEntity>  {
}