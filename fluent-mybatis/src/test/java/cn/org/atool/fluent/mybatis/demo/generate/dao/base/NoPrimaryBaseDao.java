package cn.org.atool.fluent.mybatis.demo.generate.dao.base;

import cn.org.atool.fluent.mybatis.condition.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryQuery;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryUpdate;
import org.springframework.beans.factory.annotation.Autowired;


/**
* NoPrimaryEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class NoPrimaryBaseDao extends BaseDaoImpl<NoPrimaryEntity, NoPrimaryQuery, NoPrimaryUpdate>
        implements NoPrimaryMP {

    @Autowired
    protected NoPrimaryMapper mapper;

    @Override
    public NoPrimaryMapper mapper() {
        return mapper;
    }

    @Override
    public NoPrimaryQuery query(){
        return new NoPrimaryQuery();
    }

    @Override
    public NoPrimaryUpdate updater(){
        return new NoPrimaryUpdate();
    }

    @Override
    public String findPkColumn() {
        throw new RuntimeException("undefine primary key");
    }
}