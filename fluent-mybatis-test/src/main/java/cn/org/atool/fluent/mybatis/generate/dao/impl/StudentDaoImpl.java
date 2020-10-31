package cn.org.atool.fluent.mybatis.generate.dao.impl;

import cn.org.atool.fluent.mybatis.generate.dao.base.StudentBaseDao;
import cn.org.atool.fluent.mybatis.generate.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import org.springframework.stereotype.Repository;

/**
 * StudentDaoImpl: 数据操作接口实现
 *
 * @author Powered By Fluent Mybatis
 */
@Repository
public class StudentDaoImpl extends StudentBaseDao implements StudentDao {
    @Override
    public int noOverWrite() {
        StudentQuery query = super.defaultQuery();
        StudentUpdate updater = super.defaultUpdater();
        return 10;
    }
}
