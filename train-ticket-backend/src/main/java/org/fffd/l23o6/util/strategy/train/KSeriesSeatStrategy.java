package org.fffd.l23o6.util.strategy.train;

import java.util.*;

import jakarta.annotation.Nullable;


public class KSeriesSeatStrategy extends TrainSeatStrategy {
    public static final KSeriesSeatStrategy INSTANCE = new KSeriesSeatStrategy();

    /**
     * 2023/07/05 由陈凌定义
     * 声明不同座位的票价
     */
    private final int
            _price_soft_sleeper_seat, _price_hard_sleeper_seat, _price_soft_seat, _price_hard_seat, _price_no_seat;

    /**
     * 2023/07/04 由刘辉定义
     * 便于计算的一些变量
     */
    private final int _baseIndex_SS;        // SOFT_SLEEPER_SEAT 的 baseIndex
    private final int _baseIndex_HS;        // HARD_SLEEPER_SEAT 的 baseIndex
    private final int _baseIndex_S;        // SOFT_SEAT 的 baseIndex
    private final int _baseIndex_H;        // HARD_SEAT 的 baseIndex
    private final int _sumOfSeats;         // 座位总数

    private final Map<Integer, String> SOFT_SLEEPER_SEAT_MAP = new HashMap<>();
    private final Map<String, Integer> SOFT_SLEEPER_SEAT_MAP_REVERSE = new HashMap<>();
    private final Map<Integer, String> HARD_SLEEPER_SEAT_MAP = new HashMap<>();
    private final Map<String, Integer> HARD_SLEEPER_SEAT_MAP_REVERSE = new HashMap<>();
    private final Map<Integer, String> SOFT_SEAT_MAP = new HashMap<>();
    private final Map<String, Integer> SOFT_SEAT_MAP_REVERSE = new HashMap<>();
    private final Map<Integer, String> HARD_SEAT_MAP = new HashMap<>();
    private final Map<String, Integer> HARD_SEAT_MAP_REVERSE = new HashMap<>();

    private final Map<KSeriesSeatType, Map<Integer, String>> TYPE_MAP = new HashMap<>() {{
        put(KSeriesSeatType.SOFT_SLEEPER_SEAT, SOFT_SLEEPER_SEAT_MAP);
        put(KSeriesSeatType.HARD_SLEEPER_SEAT, HARD_SLEEPER_SEAT_MAP);
        put(KSeriesSeatType.SOFT_SEAT, SOFT_SEAT_MAP);
        put(KSeriesSeatType.HARD_SEAT, HARD_SEAT_MAP);
    }};


    /**
     * 2023/07/04 由 刘辉 修改
     * 添加了反表
     */
    private KSeriesSeatStrategy() {

        int counter = 0;

        _baseIndex_SS = counter;
        for (String s : Arrays.asList("软卧1号上铺", "软卧2号下铺", "软卧3号上铺", "软卧4号上铺", "软卧5号上铺", "软卧6号下铺", "软卧7号上铺", "软卧8号上铺")) {
            SOFT_SLEEPER_SEAT_MAP.put(counter, s);
            SOFT_SLEEPER_SEAT_MAP_REVERSE.put(s, counter);
            ++counter;
        }

        _baseIndex_HS = counter;
        for (String s : Arrays.asList("硬卧1号上铺", "硬卧2号中铺", "硬卧3号下铺", "硬卧4号上铺", "硬卧5号中铺", "硬卧6号下铺", "硬卧7号上铺", "硬卧8号中铺", "硬卧9号下铺", "硬卧10号上铺", "硬卧11号中铺", "硬卧12号下铺")) {
            HARD_SLEEPER_SEAT_MAP.put(counter, s);
            HARD_SLEEPER_SEAT_MAP_REVERSE.put(s, counter);
            ++counter;
        }

        _baseIndex_S = counter;
        for (String s : Arrays.asList("1车1座", "1车2座", "1车3座", "1车4座", "1车5座", "1车6座", "1车7座", "1车8座", "2车1座", "2车2座", "2车3座", "2车4座", "2车5座", "2车6座", "2车7座", "2车8座")) {
            SOFT_SEAT_MAP.put(counter, s);
            SOFT_SEAT_MAP_REVERSE.put(s, counter);
            ++counter;
        }

        _baseIndex_H = counter;
        for (String s : Arrays.asList("3车1座", "3车2座", "3车3座", "3车4座", "3车5座", "3车6座", "3车7座", "3车8座", "3车9座", "3车10座", "4车1座", "4车2座", "4车3座", "4车4座", "4车5座", "4车6座", "4车7座", "4车8座", "4车9座", "4车10座")) {
            HARD_SEAT_MAP.put(counter, s);
            HARD_SEAT_MAP_REVERSE.put(s, counter);
            ++counter;
        }

        _sumOfSeats = counter;

        _price_soft_sleeper_seat = 120;
        _price_hard_sleeper_seat = 80;
        _price_soft_seat = 60;
        _price_hard_seat = 40;
        _price_no_seat = 40;
    }


    private int _countLeftSeat(int startStationIndex, int endStationIndex, boolean[][] seatMap, int startSeat, int endSeat) {
        int count = 0;
        for (int seat = startSeat; seat <= endSeat; ++seat) {
            boolean seatOccupied = false;
            for (int atomRoute = startStationIndex; atomRoute < endStationIndex; ++atomRoute) {
                if (seatMap[atomRoute][seat]) {
                    seatOccupied = true;
                    break;
                }
            }
            if (!seatOccupied) {
                ++count;
            }
        }
        return count;
    }


    public enum KSeriesSeatType implements SeatType {
        SOFT_SLEEPER_SEAT("软卧"), HARD_SLEEPER_SEAT("硬卧"), SOFT_SEAT("软座"), HARD_SEAT("硬座"), NO_SEAT("无座");
        private final String text;
        KSeriesSeatType(String text){
            this.text=text;
        }
        public String getText() {
            return this.text;
        }
        public static KSeriesSeatType fromString(String text) {
            for (KSeriesSeatType b : KSeriesSeatType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }


    /**
     * 2023/07/04 由 刘辉 实现
     * 作用: 分配座位
     * 实现: 1. 不考虑并发
     *      2. 在 OrderServiceImpl 中 createOrder 的时刻, 立刻查询从 startStation 到 endStation 有无 type 类型的一个空座位
     *      3. 顺序检索一辆车 type 类型的座位, 顺序分配座位
     * @param startStationIndex 起始站点
     * @param endStationIndex   结束站点
     * @param seatType          座位类型
     * @param seatMap           train.getSeats(), 一辆车的座位表
     * @return                  座位名称
     */
    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, String seatType, boolean[][] seatMap) {
        if (seatType.equalsIgnoreCase("无座")) {
            return seatType;
        }
        // 获取座位类型 type 对应的 baseIndex 和 upperIndex 以便确定检索区间
        final int  baseIndex, upperIndex;

        KSeriesSeatType kSeriesSeatType = KSeriesSeatType.fromString(seatType);
        if (kSeriesSeatType == null) {
            return null;
        }
        switch (kSeriesSeatType) {
            case SOFT_SLEEPER_SEAT:
                baseIndex = _baseIndex_SS;
                upperIndex = _baseIndex_HS - 1;
                break;
            case HARD_SLEEPER_SEAT:
                baseIndex = _baseIndex_HS;
                upperIndex = _baseIndex_S - 1;
                break;
            case SOFT_SEAT:
                baseIndex = _baseIndex_S;
                upperIndex = _baseIndex_H - 1;
                break;
            case HARD_SEAT:
                baseIndex = _baseIndex_H;
                upperIndex = _sumOfSeats - 1;
                break;
            default:
                return null;

        }

        // 顺序遍历所有座位
        for (int seat = baseIndex; seat <= upperIndex; ++seat) {
            boolean seatOccupied = false;
            // 检查 startStation 到 endStation 之间, 该座位是否被占用
            for (int atomRoute = startStationIndex; atomRoute < endStationIndex; ++atomRoute) {
                if (seatMap[atomRoute][seat]) {
                    seatOccupied = true;
                    break;
                }
            }
            // 座位未被占用, 直接分配该座位
            if (!seatOccupied) {
                return TYPE_MAP.get(kSeriesSeatType).get(seat);
            }
        }

        return null;
    }

    /**
     * 2023/07/04 由 陈凌 实现
     * @param startStationIndex 出发站
     * @param endStationIndex   到达站
     * @param seatMap           座位表
     * @return                  座位数量
     */
    public Map<String, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        Map<String, Integer> seatCount = new HashMap<>();

        int SOFT_SLEEPER_SEAT_COUNT = _countLeftSeat(startStationIndex, endStationIndex, seatMap, _baseIndex_SS, _baseIndex_HS - 1);
        int HARD_SLEEPER_SEAT_COUNT = _countLeftSeat(startStationIndex, endStationIndex, seatMap, _baseIndex_HS, _baseIndex_S - 1);
        int SOFT_SEAT_COUNT = _countLeftSeat(startStationIndex, endStationIndex, seatMap, _baseIndex_S, _baseIndex_H - 1);
        int HARD_SEAT_COUNT = _countLeftSeat(startStationIndex, endStationIndex, seatMap, _baseIndex_H, _sumOfSeats - 1);

        seatCount.put(KSeriesSeatStrategy.KSeriesSeatType.SOFT_SLEEPER_SEAT.getText(), SOFT_SLEEPER_SEAT_COUNT);
        seatCount.put(KSeriesSeatStrategy.KSeriesSeatType.HARD_SLEEPER_SEAT.getText(), HARD_SLEEPER_SEAT_COUNT);
        seatCount.put(KSeriesSeatStrategy.KSeriesSeatType.SOFT_SEAT.getText(), SOFT_SEAT_COUNT);
        seatCount.put(KSeriesSeatStrategy.KSeriesSeatType.HARD_SEAT.getText(), HARD_SEAT_COUNT);

        return seatCount;
    }

    public Integer getSeatTypePrice(String seatType) {
        if (seatType.equalsIgnoreCase("软卧")) {
            return _price_soft_sleeper_seat;
        }
        if (seatType.equalsIgnoreCase("硬卧")) {
            return _price_hard_sleeper_seat;
        }
        if (seatType.equalsIgnoreCase("软座")) {
            return _price_soft_seat;
        }
        if (seatType.equalsIgnoreCase("硬座")) {
            return _price_hard_seat;
        }
        return null;
    }

    /**
     * 2023/07/05 由陈凌实现
     * 作用： 返回座位对应的票价
     * 使用： payOrder 中被调用
     * @param seat 座位名称
     */
    public Integer getSeatPrice(String seat) {
        if (seat.equalsIgnoreCase("无座")) {
            return _price_no_seat;
        }

        Integer price;
        price = SOFT_SLEEPER_SEAT_MAP_REVERSE.get(seat);
        if (price != null) {
            return _price_soft_sleeper_seat;
        }
        price = HARD_SLEEPER_SEAT_MAP_REVERSE.get(seat);
        if (price != null) {
            return _price_hard_sleeper_seat;
        }
        price = SOFT_SEAT_MAP_REVERSE.get(seat);
        if (price != null) {
            return _price_soft_seat;
        }
        price = HARD_SEAT_MAP_REVERSE.get(seat);
        if (price != null) {
            return _price_hard_seat;
        }

        return null;
    }

    /**
     * 2023/07/04 由 刘辉 实现
     * @param startStationIndex 出发站
     * @param endStationIndex   终点站
     * @param seat              座位的名字
     * @param seatStatus        座位新的状态, true为占用, false为未占用
     * @param seatMap           车实体的座位表
     * @return                  新的座位表
     */
    public boolean[][] updateSeatMap(int startStationIndex, int endStationIndex, String seat, boolean seatStatus, boolean[][] seatMap) {
        if (seat == null || seat.equalsIgnoreCase("无座")) {
            return seatMap;
        }

        Integer seatIndex = SOFT_SLEEPER_SEAT_MAP_REVERSE.get(seat);
        if (seatIndex == null) {
            seatIndex = HARD_SLEEPER_SEAT_MAP_REVERSE.get(seat);
        }
        if (seatIndex == null) {
            seatIndex = SOFT_SEAT_MAP_REVERSE.get(seat);
        }
        if (seatIndex == null) {
            seatIndex = HARD_SEAT_MAP_REVERSE.get(seat);
        }
        if (seatIndex == null) {
            throw new RuntimeException("No such seat! Error occurs in GSeriesSeatStrategy.java updateSeatMap()");
        }

        for (int station = startStationIndex; station < endStationIndex; ++station) {
            seatMap[station][seatIndex] = seatStatus;
        }

        return seatMap;
    }

    public boolean[][] initSeatMap(int stationCount) {
        return new boolean[stationCount - 1][SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size() + SOFT_SEAT_MAP.size() + HARD_SEAT_MAP.size()];
    }
}
