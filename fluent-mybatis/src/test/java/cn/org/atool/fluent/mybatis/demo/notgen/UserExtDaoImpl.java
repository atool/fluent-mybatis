package cn.org.atool.fluent.mybatis.demo.notgen;

import cn.org.atool.fluent.mybatis.demo.generate.dao.impl.UserDaoImpl;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP;
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
        return super.selectFields(
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
            .execute(super::selectList);
    }

    @Override
    public List<String> selectObjs(Long... ids) {
        return super.selectObjs(
            super.query().select(UserMP.userName)
                .where.id().in(ids).end(),
            (map) -> (String) map.get(UserMP.userName.column)
        );
    }

    @Override
    public List<String> selectObjs2(Long... ids) {
        return super.selectObjs(
            super.query().select(UserMP.userName, UserMP.age)
                .where.id().in(ids).end(),
            (map) -> (String) map.get(UserMP.userName.column)
        );
    }

    @Override
    public UserEntity selectOne(String likeName) {
        return super.query().where.userName().like(likeName).end().execute(super::selectOne);
    }

    @Override
    public String selectOne(long id) {
        return super.selectOne(super.query()
                .where.id().eq(id).end(),
            UserEntity::getUserName);
    }

    @Override
    public void deleteByQuery(String username) {
        super.query()
            .where.userName().eq(username).end()
            .execute(super::deleteByQuery);
    }

    @Override
    public void deleteByQuery(String... userNames) {
        super.query()
            .where.userName().in(userNames).end()
            .execute(super::deleteByQuery);
    }

    @Override
    public void updateUserNameById(String newUserName, long id) {
        super.updater()
            .set.userName().is(newUserName)
            .end()
            .where.id().eq(id).end()
            .execute(super::update);
    }
}