package org.fffd.l23o6.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.fffd.l23o6.service.OrderService;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.TrainSeatStrategy;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    /**
     * 2023/07/04 刘辉 修改
     * 实现创建订单服务
     * @param username      用户名
     * @param trainId       车次 ID
     * @param fromStationId 出发站 ID
     * @param toStationId   到达站 ID
     * @param seatType      座位类型
     * @param seatNumber    座位数量
     * @return Long 类型的订单号
     */
    public Long createOrder(String username, Long trainId, Long fromStationId, Long toStationId, String seatType,
            Long seatNumber) {
        // 1. 查找数据库， 转化数据类型
        Long userId = userDao.findByUsername(username).getId();

        if (trainDao.findById(trainId).isEmpty()) {
            throw new NoSuchElementException("No Such TrainEntity");
        }
        TrainEntity train = trainDao.findById(trainId).get();

        System.err.println(Arrays.deepToString(train.getSeats()));

        if (routeDao.findById(train.getRouteId()).isEmpty()) {
            throw new NoSuchElementException("No Such RouteEntity");
        }
        RouteEntity route = routeDao.findById(train.getRouteId()).get();

        int startStationIndex = route.getStationIds().indexOf(fromStationId);
        int endStationIndex = route.getStationIds().indexOf(toStationId);

        System.err.println("creat前");
        System.err.println(Arrays.deepToString(train.getSeats()));

        // 2. 检索座位
        String seat;
        TrainSeatStrategy seatStrategy = null;
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                seatStrategy = GSeriesSeatStrategy.INSTANCE;
                break;
            case NORMAL_SPEED:
                seatStrategy = KSeriesSeatStrategy.INSTANCE;
                break;
        }
        seat = seatStrategy.allocSeat(startStationIndex, endStationIndex, seatType, train.getSeats());

        train.setSeats(seatStrategy.updateSeatMap(startStationIndex, endStationIndex, seat, true, train.getSeats()));

        System.err.println("creat后");
        System.err.println(Arrays.deepToString(train.getSeats()));
        System.err.println(startStationIndex);
        System.err.println(endStationIndex);
        System.err.println(seat);
        System.err.println();

        if (seat == null) {
            throw new BizException(BizError.OUT_OF_SEAT);
        }
        
        // 3. 创建订单
        OrderEntity order = OrderEntity.builder().trainId(trainId).userId(userId).seat(seat)
                .status(OrderStatus.PENDING_PAYMENT).arrivalStationId(toStationId).departureStationId(fromStationId)
                .build();
        train.setUpdatedAt(null);// force it to update

        // 4. 更新数据库
        trainDao.save(train);
        orderDao.save(order);
        
        // 5. 返回
        return order.getId();
    }

    /**
     * 列出指定用户的订单
     * @param username 用户名
     * @return 订单列表
     */
    public List<OrderVO> listOrders(String username) {
        // 1. 查找数据库，转换数据类型
        Long userId = userDao.findByUsername(username).getId();
        List<OrderEntity> orders = orderDao.findByUserId(userId);
        orders.sort((o1,o2)-> o2.getId().compareTo(o1.getId()));
        // 2. 返回
        return orders.stream().map(order -> {
            TrainEntity trainEntity = trainDao.findById(order.getTrainId()).get();
            RouteEntity routeEntity = routeDao.findById(trainEntity.getRouteId()).get();

            if (trainDao.findById(order.getTrainId()).isEmpty()) {
                throw new NoSuchElementException("No Such TrainEntity");
            }
            if (routeDao.findById(trainEntity.getRouteId()).isEmpty()) {
                throw new NoSuchElementException("No Such RouteEntity");
            }
            int startIndex = routeEntity.getStationIds().indexOf(order.getDepartureStationId());
            int endIndex = routeEntity.getStationIds().indexOf(order.getArrivalStationId());
            return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                    .seat(order.getSeat()).status(order.getStatus().getText())
                    .createdAt(order.getCreatedAt())
                    .startStationId(order.getDepartureStationId())
                    .endStationId(order.getArrivalStationId())
                    .departureTime(trainEntity.getDepartureTimes().get(startIndex))
                    .arrivalTime(trainEntity.getArrivalTimes().get(endIndex))
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 获取指定 ID 的订单
     * @param id 订单ID
     * @return 订单 VO
     */
    public OrderVO getOrder(Long id) {
        // 1. 查找数据库
        OrderEntity order = orderDao.findById(id).get();
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        order = orderDao.findById(id).get();
        // 2. 返回
        int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
        return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                .seat(order.getSeat()).status(order.getStatus().getText())
                .createdAt(order.getCreatedAt())
                .startStationId(order.getDepartureStationId())
                .endStationId(order.getArrivalStationId())
                .departureTime(train.getDepartureTimes().get(startIndex))
                .arrivalTime(train.getArrivalTimes().get(endIndex))
                .build();
    }

    /**
     * 2023/07/04 由 程智镝 实现, 07/07由刘辉修改注释和 3.的语序
     * 功能：取消指定 ID 的订单
     * @param id 订单ID
     */
    public void cancelOrder(Long id) {
        // fixed TODO: refund user's money and credits if needed
        
        // 1.检索数据库
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }
        
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        
        // 2.获取根据订单信息，获取座位
        int startStationIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endStationIndex = route.getStationIds().indexOf(order.getArrivalStationId());
        String seat = order.getSeat();

        
        // 3.取消已分配的座位,获得新的座位表
        boolean[][] newTable  = train.getSeats();
        if (!Objects.equals(seat, "无座")) {
            TrainSeatStrategy seatStrategy = null;
            switch (train.getTrainType()) {
                case HIGH_SPEED:
                    seatStrategy = GSeriesSeatStrategy.INSTANCE;
                    break;
                case NORMAL_SPEED:
                    seatStrategy = KSeriesSeatStrategy.INSTANCE;
                    break;
            }

            System.err.println("cancel");
            System.err.println(Arrays.deepToString(newTable));
            System.err.println(startStationIndex);
            System.err.println(endStationIndex);
            System.err.println(seat);
            newTable = seatStrategy.updateSeatMap(startStationIndex, endStationIndex, seat, false, train.getSeats());
            System.err.println("newTable");
            System.err.println(Arrays.deepToString(newTable));
        }


        // 4.更新实体
        train.setSeats(newTable);
        order.setStatus(OrderStatus.CANCELLED);

        System.err.println("update");
        System.err.println(Arrays.deepToString(train.getSeats()));

        train.setUpdatedAt(null);// force it to update
        // 5.更新数据库
        trainDao.save(train);
        orderDao.save(order);
    }

    /**
     * 07/05由多人实现, 07/07 由刘辉修改,并更新文档和注释
     * 更改订单状态
     * @param id Order的ID
     */
    public void payOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderDao.save(order);
    }
}
