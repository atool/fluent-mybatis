package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.generate.entity.MemberEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.refs.IEntityRelation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityRelation implements IEntityRelation {
    @Override
    public List<MemberEntity> findExFriendsOfMemberEntity(MemberEntity entity) {
        return null;
    }

    @Override
    public MemberEntity findCurrFriendOfMemberEntity(MemberEntity entity) {
        return null;
    }

    @Override
    public StudentScoreEntity findEnglishScoreOfStudentEntity(StudentEntity entity) {
        return new StudentScoreQuery()
            .where.studentId().eq(entity.getId())
            .and.subject().eq("EN")
            .and.isDeleted().eq(false)
            .and.env().eq(entity.getEnv()).end()
            .limit(1)
            .to().findOne().orElse(null);
    }
}