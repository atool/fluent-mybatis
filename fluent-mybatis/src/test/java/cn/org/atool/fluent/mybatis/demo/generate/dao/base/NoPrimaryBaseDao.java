package cn.org.atool.fluent.mybatis.demo.generate.dao.base;

import cn.org.atool.fluent.mybatis.base.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryEntityQuery;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryEntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;


/**
* NoPrimaryEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class NoPrimaryBaseDao extends BaseDaoImpl<NoPrimaryEntity, NoPrimaryEntityQuery, NoPrimaryEntityUpdate>
        implements NoPrimaryMP {

    @Autowired
    protected NoPrimaryMapper mapper;

    @Override
    public NoPrimaryMapper mapper() {
        return mapper;
    }

    @Override
    public NoPrimaryEntityQuery query(){
        return new NoPrimaryEntityQuery();
    }

    @Override
    public NoPrimaryEntityUpdate update(){
        return new NoPrimaryEntityUpdate();
    }

    @Override
    public String findPkColumn() {
        throw new RuntimeException("undefine primary key");
    }
}