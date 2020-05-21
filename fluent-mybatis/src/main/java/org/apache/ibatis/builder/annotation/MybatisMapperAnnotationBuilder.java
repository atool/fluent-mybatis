package org.apache.ibatis.builder.annotation;

import cn.org.atool.fluent.mybatis.util.GlobalConfigUtils;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.MybatisConfiguration;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 继承 {@link MapperAnnotationBuilder#parse}
 * <p>
 * 注入fluentMybatis的动态xml：fluentMybatisHack
 * 其他方法因为要引用，但父类是私有的，不想重复拷贝黏贴，只有黑科技反射一把
 * </p>
 *
 * @author darui.wu
 */
public class MybatisMapperAnnotationBuilder extends MapperAnnotationBuilder {

    private final MybatisConfiguration configuration;
    private final MapperBuilderAssistant assistant;
    private final Class<?> type;

    public MybatisMapperAnnotationBuilder(MybatisConfiguration configuration, Class<?> type) {
        super(configuration, type);
        String resource = type.getName().replace('.', '/') + ".java (best guess)";
        this.assistant = new MapperBuilderAssistant(configuration, resource);
        this.configuration = configuration;
        this.type = type;
    }

    @Override
    public void parse() {
        String resource = type.toString();
        if (!configuration.isResourceLoaded(resource)) {
            this.loadXmlResource();
            configuration.addLoadedResource(resource);
            final String typeName = type.getName();
            assistant.setCurrentNamespace(typeName);
            this.parseCache();
            this.parseCacheRef();
            Method[] methods = type.getMethods();
            for (Method method : methods) {
                try {
                    // issue #237
                    if (!method.isBridge()) {
                        this.parseStatement(method);
                    }
                } catch (IncompleteElementException e) {
                    configuration.addIncompleteMethod(new MethodResolver(this, method));
                }
            }
            this.fluentMybatisHack();
        }
        this.parsePendingMethods();
    }

    /**
     * 注入 CURD 动态 SQL , 放在在最后, because 可能会有人会用注解重写sql
     */
    private void fluentMybatisHack() {
        if (GlobalConfigUtils.isSupperMapperChildren(configuration, type)) {
            GlobalConfigUtils.getSqlInjector(configuration).inspectInject(assistant, type);
        }
    }



    private void parsePendingMethods() {
        this.invoke("parsePendingMethods");
    }

    private void loadXmlResource() {
        this.invoke("loadXmlResource");
    }

    private void parseCache() {
        this.invoke("parseCache");
    }

    private void parseCacheRef() {
        this.invoke("parseCacheRef");
    }

    static Map<String, Method> Methods = new ConcurrentHashMap<>(8);

    private Object invoke(String methodName) {
        try {
            if (!Methods.containsKey(methodName)) {
                Method method = MapperAnnotationBuilder.class.getDeclaredMethod(methodName);
                method.setAccessible(true);
                Methods.put(methodName, method);
            }
            return Methods.get(methodName).invoke(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}