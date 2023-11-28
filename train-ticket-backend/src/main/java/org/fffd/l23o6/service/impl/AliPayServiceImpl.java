package org.fffd.l23o6.service.impl;

import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.config.AliPay;
import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.service.AliPayService;
import org.fffd.l23o6.util.strategy.mileagePoints.MileagePointsStrategy;
import org.fffd.l23o6.util.strategy.mileagePoints.MileagePointsUseAllStrategy;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AliPayServiceImpl implements AliPayService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    @Override
    public double getPayParams(String id) {
        // 1. 检索数据库
        OrderEntity order = orderDao.findById(Long.parseLong(id)).get();
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        UserEntity user = userDao.findById(order.getUserId()).get();

        // 2. 读取信息
        int startStationIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endStationIndex = route.getStationIds().indexOf(order.getArrivalStationId());

        // 3. 计算票价
        double price;
        int atomPrice = 0;
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                atomPrice = GSeriesSeatStrategy.INSTANCE.getSeatPrice(order.getSeat());
                break;
            case NORMAL_SPEED:
                atomPrice = KSeriesSeatStrategy.INSTANCE.getSeatPrice(order.getSeat());
                break;
        }
        // 3.1. 总票价
        price = atomPrice * (endStationIndex - startStationIndex);

        // 3.2. 使用积分后的票价
        // 程智镝实现, 刘辉修改
        double newPrice;
        Integer nowPoints = user.getMileagePoints();
        if (nowPoints == null) {
            nowPoints = 0;
        }
        MileagePointsStrategy mileagePointsStrategy;

        // 使用全部积分策略
        mileagePointsStrategy = MileagePointsUseAllStrategy.INSTANCE;
        MileagePointsStrategy.DiscountInfo discountInfo = mileagePointsStrategy.getDiscount(nowPoints);

        // 更新价格, 更新积分
        newPrice = price - discountInfo.discount;
        user.setMileagePoints(discountInfo.mileagePoints + 1000 * (endStationIndex - startStationIndex));

        userDao.save(user);
        return newPrice;
    }
}
