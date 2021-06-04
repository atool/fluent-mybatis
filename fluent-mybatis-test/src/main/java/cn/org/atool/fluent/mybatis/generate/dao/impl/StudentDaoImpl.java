package cn.org.atool.fluent.mybatis.generate.dao.impl;

import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.generate.dao.base.StudentBaseDao;
import cn.org.atool.fluent.mybatis.generate.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * StudentDaoImpl: 数据操作接口实现
 *
 * @author Powered By Fluent Mybatis
 */
@Repository
public class StudentDaoImpl extends StudentBaseDao implements StudentDao {
    @Override
    public int noOverWrite() {
        return 10;
    }

    @Override
    public List<StudentEntity> findStudentsByName(String name) {
        return super.listEntity(super.query().where.userName().like(name).end());
    }

    @Override
    public void updateAddressAndAgeById(StudentEntity... entities) {
        List<IUpdate> updates = Arrays.stream(entities).map(student -> super.updater()
            .update.address().is(student.getAddress())
            .set.age().is(student.getAge())
            .end()
            .where.id().eq(student.getId())
            .end()
        ).collect(Collectors.toList());
        super.updateBy(updates);
    }

    @Override
    public Optional<StudentEntity> findOne(StudentQuery query) {
        return super.findOne(StudentEntity.class, query);
    }
}
