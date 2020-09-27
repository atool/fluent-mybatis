package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.generate.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;

import java.util.List;

public interface UserExtDao extends UserDao {
    int count(String userName);

    List<String> selectFields(Long... ids);

    List<UserEntity> selectList(Long... ids);

    List<String> selectObjs(Long... ids);

    List<String> selectObjs2(Long... ids);

    UserEntity selectOne(String likeName);

    String selectOne(long id);

    void deleteByQuery(String username);

    void deleteByQuery(String... userNames);

    void updateUserNameById(String newUserName, long id);
}