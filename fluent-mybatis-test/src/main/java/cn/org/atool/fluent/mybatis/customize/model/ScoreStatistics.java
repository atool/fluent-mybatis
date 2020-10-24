package cn.org.atool.fluent.mybatis.customize.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreStatistics {
    private int schoolTerm;

    private String subject;

    private long count;

    private Integer minScore;

    private Integer maxScore;

    private BigDecimal avgScore;
}
