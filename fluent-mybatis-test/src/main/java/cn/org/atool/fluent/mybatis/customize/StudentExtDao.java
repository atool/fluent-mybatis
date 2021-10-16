package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.base.IBaseDao;

import java.util.List;

public interface StudentExtDao extends IBaseDao<StudentEntity> {
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