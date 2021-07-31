package cn.org.atool.fluent.mybatis.metadata.feature;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import static cn.org.atool.fluent.mybatis.metadata.feature.EscapeExpress.NONE_ESCAPE;
import static cn.org.atool.fluent.mybatis.metadata.feature.PagedFormat.MYSQL_LIMIT;

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

    private PagedFormat paged;

    private String seq;

    private boolean before = false;

    public DbFeature(String name) {
        this(name, NONE_ESCAPE, MYSQL_LIMIT, null);
    }

    public DbFeature(String name, PagedFormat paged) {
        this(name, NONE_ESCAPE, paged, null);
    }

    public DbFeature(String name, PagedFormat paged, String seq) {
        this(name, NONE_ESCAPE, paged, seq);
    }

    public DbFeature(String name, EscapeExpress escape, PagedFormat paged) {
        this(name, escape, paged, null);
    }

    public DbFeature(String name, EscapeExpress escape, PagedFormat paged, String seq) {
        this.name = name;
        this.paged = paged;
        this.escape = escape;
        this.seq = seq;
    }

    private DbFeature() {
    }

    public String getPagedFormat() {
        return this.paged.getFormat();
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
            .setSeq(this.seq)
            .setBefore(this.before);
    }
}