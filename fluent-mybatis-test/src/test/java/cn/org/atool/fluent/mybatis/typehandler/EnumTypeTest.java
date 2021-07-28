package cn.org.atool.fluent.mybatis.typehandler;

import cn.org.atool.fluent.mybatis.customize.model.MyEnum;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.MyEnumTypePoJo;
import cn.org.atool.fluent.mybatis.generate.mapper.MyEnumTypeMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.MyEnumTypeQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class EnumTypeTest extends BaseTest {
    @Autowired
    MyEnumTypeMapper mapper;

    @Test
    void insert() {
        mapper.insert(new MyEnumTypePoJo()
            .setEnumNum(MyEnum.test3)
            .setEnumString(MyEnum.test2));
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO my_enum_type(`enum_num`, `enum_string`) VALUES (?, ?)");
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
            "INSERT INTO my_enum_type(`enum_num`, `enum_string`) VALUES (?, ?) , (?, ?)");
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
        mapper.updateById(new MyEnumTypePoJo()
            .setId(1L)
            .setEnumNum(MyEnum.test3)
            .setEnumString(MyEnum.test3));
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

        MyEnumTypeQuery query = MyEnumTypeQuery.query()
            .where.enumNum().eq(MyEnum.test2)
            .and.enumString().eq(MyEnum.test1)
            .end();
        List<MyEnumTypePoJo> list = mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .end("FROM my_enum_type WHERE `enum_num` = ? AND `enum_string` = ?");
        want.list(list).sizeEq(1);
    }
}
