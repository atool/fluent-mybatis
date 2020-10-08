package cn.org.atool.fluent.mybatis.generate.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * @author generate code
 * @ClassName UserDao
 * @Description UserEntity数据操作接口
 */
@Repository
public interface UserDao extends IBaseDao<UserEntity> {
    /**
     * 测试 dao类不会被重写
     *
     * @return
     */
    int noOverWrite();
}