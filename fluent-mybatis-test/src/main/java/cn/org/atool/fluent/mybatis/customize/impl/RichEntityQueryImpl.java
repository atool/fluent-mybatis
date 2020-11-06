package cn.org.atool.fluent.mybatis.customize.impl;

import cn.org.atool.fluent.mybatis.generate.Refs;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import org.springframework.stereotype.Service;

@Service
public class RichEntityQueryImpl extends Refs {
    @Override
    public StudentScoreEntity englishScoreOfStudentEntity(StudentEntity entity) {
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