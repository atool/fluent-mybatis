package cn.org.atool.fluent.mybatis.demo.dao.intf;

import cn.org.atool.fluent.mybatis.demo.entity.UserEntity;
import cn.org.atool.fluent.mybatis.base.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author generate code
 * @ClassName UserDao
 * @Description UserEntity数据操作接口
 */
@Repository
public interface UserDao extends IBaseDao<UserEntity> {
    int count(String userName);

    List<String> selectFields(Long... ids);

    List<UserEntity> selectList(Long... ids);

    List<String> selectObjs(Long... ids);

    UserEntity selectOne(String likeName);

    String selectOne(long id);

    void deleteByQuery(String username);

    void deleteByQuery(String... userNames);

    void updateUserNameById(String newUserName, long id);
}