package cn.org.atool.fluent.mybatis.test.origin.typehandler;

import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import cn.org.atool.fluent.mybatis.customize.model.MyEnum;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared4.entity.MyEnumTypePoJo;
import cn.org.atool.fluent.mybatis.generator.shared4.mapper.MyEnumTypeMapper;
import cn.org.atool.fluent.mybatis.generator.shared4.wrapper.MyEnumTypeQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.annotations.Mocks;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;
import java.util.List;

@Mocks(value = SqlProvider.class)
@SuppressWarnings("unused")
public class EnumTypeTest extends BaseTest {
    static EnumTypeTestMocks mocks = EnumTypeTestMocks.mocks;

    @Autowired
    MyEnumTypeMapper mapper;

    @Test
    void insert() {
        mapper.insert(new MyEnumTypePoJo()
            .setEnumNum(MyEnum.test3)
            .setEnumString(MyEnum.test2));
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `my_enum_type` (`enum-num`, `enum_string`, `is_deleted`) VALUES (?, ?, 0)");
        db.sqlList().wantFirstPara().eqList(MyEnum.test3, MyEnum.test2);
    }

    @Test
    void insert2() {
        ATM.dataMap.myEnumType.table().clean();
        mapper.insertBatch(Arrays.asList(new MyEnumTypePoJo()
                .setEnumNum(MyEnum.test3)
                .setEnumString(MyEnum.test2),
            new MyEnumTypePoJo()
                .setEnumNum(MyEnum.test1)
                .setEnumString(MyEnum.test2)));
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `my_enum_type` " +
            "(`enum-num`, `enum_string`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, 0), " +
            "(?, ?, 0)");
        db.sqlList().wantFirstPara().eqList(MyEnum.test3, MyEnum.test2, MyEnum.test1, MyEnum.test2);
        ATM.dataMap.myEnumType.table(2)
            .enumNum.values(2, 0)
            .enumString.values("test2", "test2")
            .eqTable();
    }

    @Test
    void updateById() {
        ATM.dataMap.myEnumType.initTable(3)
            .id.values(1, 2, 3)
            .enumNum.values(1, 0, 2)
            .enumString.values("test1", "test3", "test2")
            .cleanAndInsert();
        final String[] aSql = {""};
        mocks.SqlProvider.updateBy.restAnswer(f -> {
            String sql = f.proceed();
            aSql[0] = sql;
            return sql;
        });

        mapper.updateById(new MyEnumTypePoJo()
            .setId(1L)
            .setEnumNum(MyEnum.test3)
            .setEnumString(MyEnum.test3));
        aSql[0] = aSql[0].replaceAll("\\.variable_\\d+_\\d+,", ".var,");
        want.string(aSql[0]).eq("" +
                "UPDATE `my_enum_type` " +
                "SET `enum-num` = #{ew[0].data.parameters.var, javaType=cn.org.atool.fluent.mybatis.customize.model.MyEnum, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler},\n" +
                "`enum_string` = #{ew[0].data.parameters.var, javaType=cn.org.atool.fluent.mybatis.customize.model.MyEnum, typeHandler=org.apache.ibatis.type.EnumTypeHandler} " +
                "WHERE `id` = #{ew[0].data.parameters.var, javaType=java.lang.Long, typeHandler=org.apache.ibatis.type.LongTypeHandler}",
            StringMode.SameAsSpace);
        ATM.dataMap.myEnumType.table(3)
            .id.values(1, 2, 3)
            .enumNum.values(2, 0, 2)
            .enumString.values("test3", "test3", "test2")
            .eqTable();
    }

    @Test
    void query() {
        ATM.dataMap.myEnumType.initTable(3)
            .enumNum.values(1, 0, 2)
            .enumString.values("test1", "test3", "test2")
            .cleanAndInsert();

        MyEnumTypeQuery query = MyEnumTypeQuery.emptyQuery()
            .where.enumNum().eq(MyEnum.test2)
            .and.enumString().eq(MyEnum.test1)
            .end();
        List<MyEnumTypePoJo> list = mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .end("FROM `my_enum_type` WHERE `enum-num` = ? AND `enum_string` = ?");
        want.list(list).sizeEq(1);
    }

    @Test
    void findById() {
        final String[] aSql = {""};
        mocks.SqlProvider.listEntity.restAnswer(f -> {
            String sql = f.proceed();
            aSql[0] = sql;
            return sql;
        });
        mapper.findById(1L);

        aSql[0] = aSql[0].replaceAll("\\.variable_\\d+_\\d+,", ".var,");
        want.string(aSql[0]).end("" +
                "WHERE `id` = #{ew.data.parameters.var, javaType=java.lang.Long, typeHandler=org.apache.ibatis.type.LongTypeHandler}",
            StringMode.SameAsSpace);
    }

    @Test
    void deleteById() {
        final String[] aSql = {""};
        mocks.SqlProvider.delete.restAnswer(f -> {
            String sql = f.proceed();
            aSql[0] = sql;
            return sql;
        });
        mapper.deleteById(1L, 2L);

        aSql[0] = aSql[0].replaceAll("\\.variable_\\d+_\\d+,", ".var,");
        want.string(aSql[0]).eq("" +
            "DELETE FROM `my_enum_type` " +
            "WHERE `id` IN (" +
            "#{ew.data.parameters.var, javaType=java.lang.Long, typeHandler=org.apache.ibatis.type.LongTypeHandler}, " +
            "#{ew.data.parameters.var, javaType=java.lang.Long, typeHandler=org.apache.ibatis.type.LongTypeHandler}" +
            ")", StringMode.SameAsSpace);
    }

    @Test
    void logicDeleteById() {
        final String[] aSql = {""};
        mocks.SqlProvider.updateBy.restAnswer(f -> {
            String sql = f.proceed();
            aSql[0] = sql;
            return sql;
        });
        mapper.logicDeleteById(1L);
        aSql[0] = aSql[0].replaceAll("\\.variable_\\d+_\\d+", ".var");
        want.string(aSql[0]).eq("" +
                "UPDATE `my_enum_type` " +
                "SET `is_deleted` = #{ew[0].data.parameters.var} " +
                "WHERE `id` = #{ew[0].data.parameters.var, javaType=java.lang.Long, typeHandler=org.apache.ibatis.type.LongTypeHandler}"
            , StringMode.SameAsSpace);
    }

    @Test
    void updateByEntityId() {
        final String[] aSql = {""};
        mocks.SqlProvider.updateBy.restAnswer(f -> {
            String sql = f.proceed();
            aSql[0] = sql;
            return sql;
        });
        mapper.updateById(new MyEnumTypePoJo().setEnumNum(MyEnum.test2).setId(3L));
        aSql[0] = aSql[0].replaceAll("\\.variable_\\d+_\\d+,", ".var,");
        want.string(aSql[0]).eq("" +
                "UPDATE `my_enum_type` " +
                "SET `enum-num` = #{ew[0].data.parameters.var, javaType=cn.org.atool.fluent.mybatis.customize.model.MyEnum, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler} " +
                "WHERE `id` = #{ew[0].data.parameters.var, javaType=java.lang.Long, typeHandler=org.apache.ibatis.type.LongTypeHandler}",
            StringMode.SameAsSpace);
    }

    @Test
    void deleteByIds() {
        final String[] aSql = {""};
        mocks.SqlProvider.delete.restAnswer(f -> {
            String sql = f.proceed();
            aSql[0] = sql;
            return sql;
        });
        mapper.deleteByIds(list(1L));
        aSql[0] = aSql[0].replaceAll("\\.variable_\\d+_\\d+,", ".var,");
        want.string(aSql[0]).eq("" +
                "DELETE FROM `my_enum_type` " +
                "WHERE `id` = #{ew.data.parameters.var, javaType=java.lang.Long, typeHandler=org.apache.ibatis.type.LongTypeHandler}"
            , StringMode.SameAsSpace);
    }
}
