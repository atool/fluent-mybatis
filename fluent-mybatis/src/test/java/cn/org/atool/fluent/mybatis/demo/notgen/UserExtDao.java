package cn.org.atool.fluent.mybatis.demo.notgen;

import cn.org.atool.fluent.mybatis.demo.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.demo.entity.UserEntity;

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
