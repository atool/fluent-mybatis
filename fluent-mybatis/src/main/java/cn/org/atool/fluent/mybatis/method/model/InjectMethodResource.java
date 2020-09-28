package cn.org.atool.fluent.mybatis.method.model;

import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;

/**
 * InjectMethodResource
 *
 * @author darui.wu
 * @create 2020/5/26 10:41 上午
 */
public class InjectMethodResource extends InputStreamResource {
    private String namespace;

    public InjectMethodResource(Class mapper, String xml) {
        super(new ByteArrayInputStream(xml.getBytes()));
        this.namespace = mapper.getName();
    }

    /**
     * <pre>
     *  必须定义toString方法
     *  {@link org.apache.ibatis.builder.xml.XMLMapperBuilder#parse}
     *  XMLMapperBuilder#parse通过resource.toString作为标识判断资源是否已经加载
     *  具体判断: configuration.isResourceLoaded(resource)
     * </pre>
     *
     * @return
     */
    @Override
    public String toString() {
        return "InjectMethodResource{" + namespace + "}";
    }
}