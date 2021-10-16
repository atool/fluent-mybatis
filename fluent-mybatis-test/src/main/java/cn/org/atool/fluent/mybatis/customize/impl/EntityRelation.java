package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.TeacherEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentTeacherRelationQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.TeacherQuery;
import cn.org.atool.fluent.mybatis.generator.shared3.entity.MemberEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class EntityRelation implements
    IEntityRelation,
    cn.org.atool.fluent.mybatis.generator.shared3.IEntityRelation {
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

    @Override
    public List<TeacherEntity> findTeacherListOfStudentEntity(StudentEntity student) {
        return TeacherQuery.query()
            .where.id().in(
                StudentTeacherRelationQuery.query()
                    .select.teacherId().end()
                    .where.studentId().eq(student.getId())
                    .end()
            ).end()
            .to().listEntity();
    }

    @Override
    public List<StudentEntity> findStudentListOfTeacherEntity(TeacherEntity teacher) {
        return StudentQuery.query()
            .where.id().in(
                StudentTeacherRelationQuery.query()
                    .select.studentId().end()
                    .where.teacherId().eq(teacher.getId())
                    .end()
            ).end()
            .to().listEntity();
    }

    @Override
    public void initialize() {
        IEntityRelation.super.initialize();
        cn.org.atool.fluent.mybatis.generator.shared3.IEntityRelation.super.initialize();
    }
}