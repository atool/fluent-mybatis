package cn.org.atool.fluent.mybatis.method2;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.method.model.InjectMapperXml;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

/**
 * MethodInjectTest
 *
 * @author:darui.wu Created by darui.wu on 2020/5/25.
 */
class InjectMapperXmlTest extends Test4J {

    @Test
    void buildMapperXml() {
        String xml = InjectMapperXml.buildMapperXml(UserMapper.class);
        System.out.println("=================");
        System.out.println(xml);

        System.out.println("=================");
    }
}