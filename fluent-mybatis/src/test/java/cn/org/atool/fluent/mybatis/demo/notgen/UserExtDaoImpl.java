package cn.org.atool.fluent.mybatis.demo.notgen;

import cn.org.atool.fluent.mybatis.demo.generate.dao.impl.UserDaoImpl;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserExtDaoImpl extends UserDaoImpl implements UserExtDao {
    @Override
    public int count(String userName) {
        return super.query()
            .where.userName().eq(userName)
            .end()
            .execute(super::count);
    }

    @Override
    public List<String> selectFields(Long... ids) {
        return super.listObjs(
            super.query()
                .where.id().in(ids)
                .end(),
            UserEntity::getUserName
        );
    }

    @Override
    public List<UserEntity> selectList(Long... ids) {
        return super.query()
            .where.id().in(ids).end()
            .execute(super::listEntity);
    }

    @Override
    public List<String> selectObjs(Long... ids) {
        return super.listPoJos(
            super.query().select(UserMapping.userName)
                .where.id().in(ids).end(),
            (map) -> (String) map.get(UserMapping.userName.column)
        );
    }

    @Override
    public List<String> selectObjs2(Long... ids) {
        return super.listPoJos(
            super.query().select(UserMapping.userName, UserMapping.age)
                .where.id().in(ids).end(),
            (map) -> (String) map.get(UserMapping.userName.column)
        );
    }

    @Override
    public UserEntity selectOne(String likeName) {
        return super.query().where.userName().like(likeName).end().execute(super::findOne);
    }

    @Override
    public String selectOne(long id) {
        return super.findOne(super.query()
                .where.id().eq(id).end(),
            UserEntity::getUserName);
    }

    @Override
    public void deleteByQuery(String username) {
        super.query()
            .where.userName().eq(username).end()
            .execute(super::deleteBy);
    }

    @Override
    public void deleteByQuery(String... userNames) {
        super.query()
            .where.userName().in(userNames).end()
            .execute(super::deleteBy);
    }

    @Override
    public void updateUserNameById(String newUserName, long id) {
        super.updater()
            .set.userName().is(newUserName)
            .end()
            .where.id().eq(id).end()
            .execute(super::updateBy);
    }
}