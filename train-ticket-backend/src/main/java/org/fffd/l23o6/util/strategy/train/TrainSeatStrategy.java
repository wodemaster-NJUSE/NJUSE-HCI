package org.fffd.l23o6.util.strategy.train;

import jakarta.annotation.Nullable;

import java.util.Map;

public abstract class TrainSeatStrategy {

    public interface SeatType {
        String getText();
        static TrainSeatStrategy.SeatType fromString() {
            return null;
        }
    }

    /**
     * 作用: 分配座位
     * @param startStationIndex 起始站点
     * @param endStationIndex   结束站点
     * @param seatType          座位类型
     * @param seatMap           train.getSeats(), 一辆车的座位表
     * @return                  座位名称
     */
    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, String seatType, boolean[][] seatMap) {
        // TODO——DONE
        // endStationIndex - 1 = upper bound
        return null;
    }


    /**
     * 作用: 余票查询
     * @param startStationIndex 出发站点的编号
     * @param endStationIndex   到达站点的编号
     * @param seatMap           座位map
     * @return map, 由 座位等级枚举 映射到 相应的余票数量
     */
    public Map<String, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        // TODO——DONE
        return null;
    }


    /**
     * 作用: 更新 seatMap
     * @param startStationIndex 出发站
     * @param endStationIndex   终点站
     * @param seat              座位的名字
     * @param seatStatus        座位新的状态, true为占用, false为未占用
     * @param seatMap           车实体的座位表
     * @return                  新的座位表
     */
    public boolean[][] updateSeatMap(int startStationIndex, int endStationIndex, String seat, boolean seatStatus, boolean[][] seatMap) {
        return null;
    }

    /**
     * 作用: 查询座位类型对应的票价
     * @param seatType 座位类型
     * @return         价格
     */
    public Integer getSeatTypePrice(String seatType) {
        return null;
    }

    /**
     * 作用: 查询座位对应的票价
     * @param seat 座位
     * @return     票价
     */
    public Integer getSeatPrice(String seat) {
        return null;
    }

    /**
     * 初始化 seatMap
     * @param stationCount 车站数量
     * @return             seatMap
     */
    public boolean[][] initSeatMap(int stationCount) {
        return null;
    }
}
