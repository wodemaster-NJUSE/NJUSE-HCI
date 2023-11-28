package org.fffd.l23o6.util.strategy.mileagePoints;

import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class MileagePointsStrategyTest {

    double baseStrategy(int nowPoints) {
        double discount = 1000 * (0.001);   // 积分1000的情况
        if (nowPoints < 1000) {             // 不足1000则没有折扣, 不修改积分
            discount = 0;
        } else if (nowPoints < 3000) {      // 0.15%
            discount += (nowPoints - 1000) * (0.0015);
        } else if (nowPoints < 10000) {     // 0.2%
            discount +=  2000 * (0.0015) + (nowPoints-3000)*(0.002);
        }else if(nowPoints < 50000){
            discount +=  2000*(0.0015)+7000*(0.002)+(nowPoints-10000)*(0.0025);//0.25%
        }else {//50000以上
            discount +=  2000*(0.0015)+7000*(0.002)+40000*(0.0025)+(nowPoints-50000)*(0.003);//0.3%
        }
        return discount;
    }

    @Test
    void randomTest() {
        for (int i = 0; i < 1000; ++i) {
            int randomPoints = (int) (Math.random() * 100000);
            String message;

            String baseVal = String.valueOf(baseStrategy(randomPoints));
            String strategyVal = String.valueOf(MileagePointsUseAllStrategy.INSTANCE.getDiscount(randomPoints).discount);

            message = "points: " + randomPoints + "\nbaseVal: " + baseVal + "\nstrategyVal: " + strategyVal + "\n";
            assertEquals(message, baseVal, strategyVal);
        }
    }
}
