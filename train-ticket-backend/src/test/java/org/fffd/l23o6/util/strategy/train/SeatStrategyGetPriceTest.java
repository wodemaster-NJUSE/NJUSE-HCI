package org.fffd.l23o6.util.strategy.train;

import org.junit.jupiter.api.Test;

public class SeatStrategyGetPriceTest {
    TrainSeatStrategy seatStrategy;
    @Test
    void test() {
        seatStrategy = GSeriesSeatStrategy.INSTANCE;
        System.err.println(seatStrategy.getSeatTypePrice("商务座"));
        System.err.println(seatStrategy.getSeatTypePrice("一等座"));
        System.err.println(seatStrategy.getSeatTypePrice("二等座"));

        seatStrategy = KSeriesSeatStrategy.INSTANCE;
        System.err.println(seatStrategy.getSeatTypePrice("哈哈哈"));
        System.err.println(seatStrategy.getSeatTypePrice("软卧"));
        System.err.println(seatStrategy.getSeatTypePrice("硬卧"));
        System.err.println(seatStrategy.getSeatTypePrice("软座"));
        System.err.println(seatStrategy.getSeatTypePrice("硬座"));

    }
}
