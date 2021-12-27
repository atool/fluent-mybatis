package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.base.EntityRefKit;
import cn.org.atool.fluent.mybatis.functions.RefKey;
import cn.org.atool.fluent.mybatis.generator.shared2.IEntityRelation;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.TeacherEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentTeacherRelationQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.TeacherQuery;
import cn.org.atool.fluent.mybatis.generator.shared3.entity.MemberEntity;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.org.atool.fluent.mybatis.base.EntityRefKit.values;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
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
            .and.subject().eq("english")
            .end()
            .limit(1)
            .to().findOne().orElse(null);
    }

    public void findEnglishScoreOfStudentEntity(List<StudentEntity> entities) {
        RefKey refKey = RefKit.byEntity(StudentEntity.class).findRefKey("findStudentScoreList");
        List<StudentScoreEntity> scores = new StudentScoreQuery()
            .where.studentId().in(values(entities, StudentEntity::getId))
            .and.subject().eq("EN")
            .and.isDeleted().eq(false)
            .and.env().in(values(entities, StudentEntity::getEnv)).end()
            .to().listEntity();
        EntityRefKit.groupRelation(refKey, entities, scores);
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