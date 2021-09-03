package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.dao.base.StudentBaseDao;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.refs.FieldRef;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Component
public class StudentExtDaoImpl extends StudentBaseDao implements StudentExtDao, IBaseDao<StudentEntity> {
    @Override
    public int count(String userName) {
        return super.defaultQuery()
            .where.userName().eq(userName).end()
            .to().count();
    }

    @Override
    public List<String> selectFields(Long... ids) {
        return super.defaultQuery()
            .where.id().in(ids).end()
            .to().listEntity()
            .stream()
            .map(StudentEntity::getUserName)
            .collect(Collectors.toList());
    }

    @Override
    public List<StudentEntity> selectList(Long... ids) {
        return super.defaultQuery()
            .where.id().in(ids).end()
            .to().listEntity();
    }

    @Override
    public List<String> selectObjs(Long... ids) {
        return super.defaultQuery()
            .select.apply(FieldRef.Student.userName).end()
            .where.id().in(ids).end()
            .to().listPoJo(
                (map) -> (String) map.get(FieldRef.Student.userName.column));
    }

    @Override
    public List<String> selectObjs2(Long... ids) {
        return super.defaultQuery()
            .select.apply(FieldRef.Student.userName, FieldRef.Student.age).end()
            .where.id().in(ids).end()
            .to().listPoJo(
                (map) -> (String) map.get(FieldRef.Student.userName.column));
    }

    @Override
    public StudentEntity selectOne(String likeName) {
        return super.defaultQuery()
            .where.userName().like(likeName)
            .end()
            .to().findOne()
            .orElse(null);
    }

    @Override
    public String selectOne(long id) {
        return super.defaultQuery()
            .where.id().eq(id).end()
            .to().findOne(StudentEntity::getUserName).orElse(null);
    }

    @Override
    public void deleteByQuery(String username) {
        super.defaultQuery()
            .where.userName().eq(username).end()
            .to().delete();
    }

    @Override
    public void deleteByQuery(String... userNames) {
        super.defaultQuery()
            .where.userName().in(userNames).end()
            .to().delete();
    }

    @Override
    public void updateUserNameById(String newUserName, long id) {
        super.defaultUpdater()
            .set.userName().is(newUserName).end()
            .where.id().eq(id).end()
            .to().updateBy();
    }
}