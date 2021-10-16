package cn.org.atool.fluent.mybatis.customize.mapper;


import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;

import java.util.List;

public interface StudentBatchMapper {
    int updateBatchByIds(List<StudentEntity> list);

    void updateStudentBatch(List list);
}
