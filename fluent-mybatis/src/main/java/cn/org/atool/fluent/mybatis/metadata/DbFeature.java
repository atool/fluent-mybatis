package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.metadata.feature.EscapeExpress;
import cn.org.atool.fluent.mybatis.metadata.feature.PagedExpress;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import static cn.org.atool.fluent.mybatis.metadata.feature.EscapeExpress.NONE_ESCAPE;
import static cn.org.atool.fluent.mybatis.metadata.feature.PagedExpress.MYSQL_LIMIT;

/**
 * 数据库特性
 *
 * @author darui.wu
 */
@SuppressWarnings("unused")
@Data
@Accessors(chain = true)
@ToString
public class DbFeature {
    private String name;

    private EscapeExpress escape;

    private PagedExpress paged;

    private String seq;

    public DbFeature(String name) {
        this(name, NONE_ESCAPE, MYSQL_LIMIT, null);
    }

    public DbFeature(String name, PagedExpress paged) {
        this(name, NONE_ESCAPE, paged, null);
    }

    public DbFeature(String name, PagedExpress paged, String seq) {
        this(name, NONE_ESCAPE, paged, seq);
    }

    public DbFeature(String name, EscapeExpress escape, PagedExpress paged) {
        this(name, escape, paged, null);
    }

    public DbFeature(String name, EscapeExpress escape, PagedExpress paged, String seq) {
        this.name = name;
        this.paged = paged;
        this.escape = escape;
        this.seq = seq;
    }

    private DbFeature() {
    }

    /**
     * 复制配置
     *
     * @return DbFeature
     */
    public DbFeature copy() {
        return new DbFeature()
            .setName(this.name)
            .setEscape(this.escape)
            .setPaged(this.paged)
            .setSeq(this.seq);
    }
}