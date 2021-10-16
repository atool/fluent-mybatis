package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.base.StudentBaseDao;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.base.IBaseDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Component
public class StudentExtDaoImpl extends StudentBaseDao implements StudentExtDao, IBaseDao<StudentEntity> {
    @Override
    public int count(String userName) {
        return super.query()
            .where.userName().eq(userName).end()
            .to().count();
    }

    @Override
    public List<String> selectFields(Long... ids) {
        return super.emptyQuery()
            .where.id().in(ids).end()
            .to().listEntity()
            .stream()
            .map(StudentEntity::getUserName)
            .collect(Collectors.toList());
    }

    @Override
    public List<StudentEntity> selectList(Long... ids) {
        return super.emptyQuery()
            .where.id().in(ids).end()
            .to().listEntity();
    }

    @Override
    public List<String> selectObjs(Long... ids) {
        return super.emptyQuery()
            .select.apply(Ref.Field.Student.userName).end()
            .where.id().in(ids).end()
            .to().listPoJo(
                (map) -> (String) map.get(Ref.Field.Student.userName.column));
    }

    @Override
    public List<String> selectObjs2(Long... ids) {
        return super.emptyQuery()
            .select.apply(Ref.Field.Student.userName, Ref.Field.Student.age).end()
            .where.id().in(ids).end()
            .to().listPoJo(
                (map) -> (String) map.get(Ref.Field.Student.userName.column));
    }

    @Override
    public StudentEntity selectOne(String likeName) {
        return super.query()
            .where.userName().like(likeName)
            .end()
            .to().findOne()
            .orElse(null);
    }

    @Override
    public String selectOne(long id) {
        return super.query()
            .where.id().eq(id).end()
            .to().findOne(StudentEntity::getUserName).orElse(null);
    }

    @Override
    public void deleteByQuery(String username) {
        super.emptyQuery()
            .where.userName().eq(username).end()
            .to().delete();
    }

    @Override
    public void deleteByQuery(String... userNames) {
        super.query()
            .where.userName().in(userNames).end()
            .to().delete();
    }

    @Override
    public void updateUserNameById(String newUserName, long id) {
        super.updater()
            .set.userName().is(newUserName).end()
            .where.id().eq(id).end()
            .to().updateBy();
    }
}