package cn.org.atool.mybatis.fluent.demo.dao.intf;

import cn.org.atool.mybatis.fluent.base.IBaseDao;
import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserDao
 * @Description UserEntity数据操作接口
 *
 * @author generate code
 */
@Repository
public interface UserDao extends IBaseDao<UserEntity>  {
}