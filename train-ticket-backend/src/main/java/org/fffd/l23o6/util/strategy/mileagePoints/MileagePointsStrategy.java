package org.fffd.l23o6.util.strategy.mileagePoints;

public abstract class MileagePointsStrategy {
    /**
     * 2023/07/07 由刘辉定义
     */

    public class DiscountInfo {
        public final double discount;
        public final int mileagePoints;

        public DiscountInfo(double discount, int mileagePoints) {
            this.discount = discount;
            this.mileagePoints = mileagePoints;
        }
    }
    public DiscountInfo getDiscount(Integer nowPoints) {
        return null;
    };
}
