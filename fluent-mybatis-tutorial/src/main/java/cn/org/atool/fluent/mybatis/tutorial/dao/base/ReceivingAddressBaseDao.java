package cn.org.atool.fluent.mybatis.tutorial.dao.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.tutorial.entity.ReceivingAddressEntity;
import cn.org.atool.fluent.mybatis.tutorial.mapper.ReceivingAddressMapper;
import cn.org.atool.fluent.mybatis.tutorial.helper.ReceivingAddressMapping;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.ReceivingAddressQuery;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.ReceivingAddressUpdate;
import org.springframework.beans.factory.annotation.Autowired;


/**
* ReceivingAddressEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class ReceivingAddressBaseDao extends BaseDaoImpl<ReceivingAddressEntity, ReceivingAddressQuery, ReceivingAddressUpdate>
        implements ReceivingAddressMapping {

    @Autowired
    protected ReceivingAddressMapper mapper;

    @Override
    public ReceivingAddressMapper mapper() {
        return mapper;
    }

    @Override
    public ReceivingAddressQuery query(){
        return new ReceivingAddressQuery();
    }

    @Override
    public ReceivingAddressUpdate updater(){
        return new ReceivingAddressUpdate();
    }

    @Override
    public String findPkColumn() {
        return ReceivingAddressMapping.id.column;
    }
}