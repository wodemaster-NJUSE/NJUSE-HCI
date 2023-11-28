package org.fffd.l23o6.util.strategy.mileagePoints;

import java.util.ArrayList;

/**
 * 一次性使用全部积分的策略, 2023/07/07由刘辉定义并实现, 采用程智镝的计算方法
 */
public class MileagePointsUseAllStrategy extends MileagePointsStrategy {

    public static final MileagePointsUseAllStrategy INSTANCE = new MileagePointsUseAllStrategy();

    private final ArrayList<Integer> discountRanks = new ArrayList<>();
    private final ArrayList<Double> discountRates = new ArrayList<>();
    private final ArrayList<Integer> discountSum = new ArrayList<>();

    private MileagePointsUseAllStrategy() {
        discountRanks.add(1000);
        discountRates.add(0.001);
        discountSum.add(1);

        discountRanks.add(3000);
        discountRates.add(0.0015);
        discountSum.add(discountSum.get(discountSum.size() - 1) + 3);

        discountRanks.add(10000);
        discountRates.add(0.002);
        discountSum.add(discountSum.get(discountSum.size() - 1) + 14);

        discountRanks.add(50000);
        discountRates.add(0.0025);
        discountSum.add(discountSum.get(discountSum.size() - 1) + 100);

        discountRanks.add(Integer.MAX_VALUE);
        discountRates.add(0.003);
    }

    /**
     * 2023/07/07 由刘辉实现，采用程智镝的写法并做修改
     * @param nowPoints 现在的积分
     * @return 折扣价格和所剩积分
     */
    @Override
    public DiscountInfo getDiscount(Integer nowPoints) {
        double discount = 0;
        if (nowPoints < discountRanks.get(0)) {     // 积分小于1000
            return new DiscountInfo(discount, nowPoints);
        }

        int idx = 0;
        while (idx < discountRanks.size() && nowPoints > discountRanks.get(idx)) {
            ++idx;
        }
        discount =  discountSum.get(idx - 1) + (nowPoints - discountRanks.get(idx - 1)) * discountRates.get(idx);
        return new DiscountInfo(discount, 0);
    }
}
