package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinBuilder;
import cn.org.atool.fluent.mybatis.generate.Mappers;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RichEntityQueryImpl extends Mappers {

    @RefMethod(StudentEntity.Mt_StudentScoreList)
    public List<StudentScoreEntity> findStudentScoreListBy(StudentEntity student) {
        StudentScoreQuery scoreQuery = studentScoreDefault.defaultQuery("score")
            .selectAll()
            .where.studentId().eq(student.getId())
            .and.env().eq(student.getEnv())
            .and.isDeleted().eq(student.getIsDeleted())
            .end();
        StudentQuery studentQuery = studentDefault.defaultQuery("student", scoreQuery);
        IQuery query = JoinBuilder.from(scoreQuery)
            .join(studentQuery)
            .on((join, l, r) -> join
                .on(l.where.studentId(), r.where.id())
                .on(l.where.env(), r.where.env())
                .on(l.where.isDeleted(), r.where.isDeleted())
            ).build();
        return studentScoreMapper.listEntity(query);
    }
}