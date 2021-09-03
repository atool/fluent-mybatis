package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.utility.SqlProviderKit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.hamcrest.IWant;
import org.test4j.integration.DataProvider;

import java.util.Iterator;

@SuppressWarnings("all")
class SqlProviderTest implements IWant {

    @MethodSource("dataForAddEwParaIndex")
    @ParameterizedTest
    void addEwParaIndex(String input, String output) {
        String sql = SqlProviderKit.addEwParaIndex(input, "[0]");
        want.string(sql).eq(output);
    }

    static Iterator dataForAddEwParaIndex() {
        return new DataProvider()
            .data("#{ew.wrapperData.parameters.xxx}", "#{ew[0].wrapperData.parameters.xxx}")
            .data("#{ew.wrapperData.parameters.}", "#{ew[0].wrapperData.parameters.}")
            .data("add#{ew.wrapperData.parameters.xxx}add", "add#{ew[0].wrapperData.parameters.xxx}add")
            .data("add#{ew.wrapperData.parametersX.xxx}add", "add#{ew.wrapperData.parametersX.xxx}add");
    }
}