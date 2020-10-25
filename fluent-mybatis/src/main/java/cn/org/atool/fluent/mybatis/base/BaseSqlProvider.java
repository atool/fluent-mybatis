package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.*;
import static java.lang.String.format;

/**
 * SqlProvider: 动态SQL构造基类
 *
 * @author wudarui
 */
public abstract class BaseSqlProvider {
    /**
     * 去掉limit部分 count(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String countNoLimit(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.COUNT(data.getTable(), data);
        sql.WHERE_GROUP_BY(data);
        return sql.toString();
    }

    /**
     * count(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String count(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.COUNT(data.getTable(), data);
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据动态条件查询Entity SQL构造
     *
     * @param map
     * @return
     */
    public String listEntity(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields());
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据动态条件查询Map列表 SQL构造
     *
     * @param map
     * @return
     */
    public String listMaps(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields());
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据动态条件查询单列数据列表 SQL构造
     *
     * @param map
     * @return
     */
    public String listObjs(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields());
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据Map查询数据SQL构造
     *
     * @param map
     * @return
     */
    public String listByMap(Map map) {
        Map<String, Object> where = getParas(map, Param_CM);
        MapperSql sql = new MapperSql();
        sql.SELECT(this.tableName(), this.allFields());
        sql.WHERE(Param_CM, where);
        return sql.toString();
    }

    /**
     * 根据Id列表查询数据SQL构造
     *
     * @param map
     * @return
     */
    public String listByIds(Map map) {
        MapperSql sql = new MapperSql();
        Collection ids = getParas(map, Param_Coll);
        assertNotEmpty("PrimaryKeyList", ids);
        sql.SELECT(this.tableName(), this.allFields());
        sql.WHERE_PK_IN(this.idColumn(), ids.size());
        return sql.toString();
    }

    /**
     * 根据主键查找数据SQL构造
     *
     * @param id
     * @return
     */
    public String findById(Serializable id) {
        assertNotNull("PrimaryKey", id);
        MapperSql sql = new MapperSql();
        sql.SELECT(this.tableName(), this.allFields());
        sql.WHERE(format("%s = #{value}", this.idColumn()));
        return sql.toString();
    }

    /**
     * 根据动态条件查询一条记录SQL构造
     *
     * @param map
     * @return
     */
    public String findOne(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields());
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据主键物理删除数据SQL构造
     *
     * @param id 主键值
     * @return
     */
    public String deleteById(Serializable id) {
        MybatisUtil.assertNotNull("PrimaryKey", id);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName());
        sql.WHERE(format("%s = #{value}", this.idColumn()));
        return sql.toString();
    }

    /**
     * 根据主键列表物理删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String deleteByIds(Map map) {
        Collection ids = getParas(map, Param_Coll);
        assertNotEmpty("PrimaryKeyList", ids);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName());
        sql.WHERE_PK_IN(this.idColumn(), ids.size());
        return sql.toString();
    }

    /**
     * 按map删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String deleteByMap(Map<String, Object> map) {
        Map<String, Object> cm = getParas(map, Param_CM);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName());
        List<String> where = new ArrayList<>();
        for (String key : cm.keySet()) {
            where.add(format("%s = #{%s.%s}", key, Param_CM, key));
        }
        sql.WHERE(where);
        return sql.toString();
    }

    /**
     * 根据动态查询条件物理删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String delete(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName());
        sql.WHERE_GROUP_ORDER_BY(data);
        return sql.toString();
    }

    /**
     * update(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String updateBy(Map<String, Object> map) {
        WrapperData data = getWrapperData(map, Param_EW);
        Map<String, String> updates = data.getUpdates();
        assertNotEmpty("updates", updates);

        MapperSql sql = new MapperSql();
        sql.UPDATE(this.tableName());
        List<String> sets = this.updateDefaults(updates);
        sets.add(data.getUpdateStr());
        sql.SET(sets);
        sql.WHERE_GROUP_ORDER_BY(data);
        sql.LIMIT(data, true);
        return sql.toString();
    }

    /**
     * 构造updates中没有显式设置的默认值构造
     *
     * @param updates 显式update字段
     * @return
     */
    protected abstract List<String> updateDefaults(Map<String, String> updates);

    /**
     * 返回表名
     *
     * @return
     */
    protected abstract String tableName();

    /**
     * 主键字段名
     *
     * @return
     */
    protected abstract String idColumn();

    /**
     * 所有字段列表(以逗号隔开)
     *
     * @return
     */
    protected abstract String allFields();

    /**
     * 数据库类型
     *
     * @return
     */
    protected abstract DbType dbType();
}
