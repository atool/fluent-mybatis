package cn.org.atool.fluent.mybatis.segment;

/**
 * JoinQueryWhere
 *
 * @author wudarui
 */
public class JoinQueryWhere extends WhereBase {

    protected JoinQueryWhere(JoinQuery wrapper) {
        super(wrapper);
    }

    protected JoinQueryWhere(JoinQuery wrapper, JoinQueryWhere and) {
        super(wrapper, and);
    }

    @Override
    protected JoinQueryWhere buildOr(WhereBase and) {
        return new JoinQueryWhere((JoinQuery) this.wrapper, (JoinQueryWhere) and);
    }
}
