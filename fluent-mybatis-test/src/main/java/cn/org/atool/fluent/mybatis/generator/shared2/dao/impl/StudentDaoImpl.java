package cn.org.atool.fluent.mybatis.generator.shared2.dao.impl;

import cn.org.atool.fluent.mybatis.generator.shared2.dao.base.StudentBaseDao;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * StudentDaoImpl: 数据操作接口实现
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class StudentDaoImpl extends StudentBaseDao implements StudentDao {
    @Override
    public int noOverWrite() {
        return 10;
    }

    @Override
    public List<StudentEntity> findStudentsByName(String name) {
        return super.listEntity(super.emptyQuery().where.userName().like(name).end());
    }

    @Override
    public void updateAddressAndAgeById(StudentEntity... entities) {
        List<IUpdate> updates = Arrays.stream(entities).map(student -> super.emptyUpdater()
            .set.address().is(student.getAddress())
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
