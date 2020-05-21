package cn.org.atool.fluent.mybatis.injector;

import cn.org.atool.fluent.mybatis.method.*;
import cn.org.atool.fluent.mybatis.method.partition.DeleteSpec;
import cn.org.atool.fluent.mybatis.method.partition.UpdateSpecByQuery;

import java.util.Arrays;
import java.util.List;

/**
 * FluentMybatisSqlInjector: 替代 DefaultSqlInjector
 *
 * @author wudarui
 */
public class FluentMybatisSqlInjector extends AbstractSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return Arrays.asList(
            // insert into
            new Insert(),
            new InsertBatch(),
            // delete from
            new Delete(),
            new DeleteById(),
            new DeleteByIds(),
            new DeleteByMap(),
            // update table
            new UpdateById(),
            new UpdateByQuery(),
            // select from
            new SelectById(),
            new SelectByIds(),
            new SelectByMap(),
            new SelectOne(),
            new SelectCount(),
            new SelectList(),
            new SelectMaps(),
            new SelectObjs(),
//            new SelectPage(),
//            new SelectMapsPage(),
            //
            // new Update(),
            // partition
//            new SelectListInPartition()
            new UpdateSpecByQuery(),
            new DeleteSpec()
        );
    }
}