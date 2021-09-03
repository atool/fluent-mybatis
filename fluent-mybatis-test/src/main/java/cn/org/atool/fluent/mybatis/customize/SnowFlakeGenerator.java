package cn.org.atool.fluent.mybatis.customize;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 雪花算法id模拟
 */
public class SnowFlakeGenerator {

    static AtomicLong id = new AtomicLong(1000);

    public static long uuid() {
        return id.incrementAndGet();
    }

    public static long fake() {
        return 1L;
    }
}