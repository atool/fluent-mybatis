package cn.org.atool.fluent.mybatis.generate.dao.impl;

import cn.org.atool.fluent.mybatis.generate.dao.base.StudentBaseDao;
import cn.org.atool.fluent.mybatis.generate.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
