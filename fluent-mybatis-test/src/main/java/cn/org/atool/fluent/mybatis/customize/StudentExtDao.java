package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.dao.IDao;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;

import java.util.List;

public interface StudentExtDao extends IDao<StudentEntity> {
    int count(String userName);

    List<String> selectFields(Long... ids);

    List<StudentEntity> selectList(Long... ids);

    List<String> selectObjs(Long... ids);

    List<String> selectObjs2(Long... ids);

    StudentEntity selectOne(String likeName);

    String selectOne(long id);

    void deleteByQuery(String username);

    void deleteByQuery(String... userNames);

    void updateUserNameById(String newUserName, long id);
}