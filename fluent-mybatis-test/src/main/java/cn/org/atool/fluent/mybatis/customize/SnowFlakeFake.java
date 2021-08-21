package cn.org.atool.fluent.mybatis.customize;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 雪花算法id模拟
 */
public class SnowFlakeFake {

    static AtomicLong id = new AtomicLong(1000);

    public static long snowFlakeId() {
        return id.incrementAndGet();
    }
}
