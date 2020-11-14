package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.generate.Refs;
import cn.org.atool.fluent.mybatis.generate.entity.MemberEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppRichRefs extends Refs {
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
        StudentScoreQuery query = new StudentScoreQuery()
            .where.studentId().eq(entity.getId())
            .and.subject().eq("EN")
            .and.isDeleted().eq(false)
            .and.env().eq(entity.getEnv())
            .end()
            .limit(1);
        return studentScoreMapper.findOne(query);
    }
}