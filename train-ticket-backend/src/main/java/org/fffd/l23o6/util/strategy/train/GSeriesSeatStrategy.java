package org.fffd.l23o6.util.strategy.train;

import java.util.*;

import jakarta.annotation.Nullable;


public class GSeriesSeatStrategy extends TrainSeatStrategy {
    public static final GSeriesSeatStrategy INSTANCE = new GSeriesSeatStrategy();

    /**
     * 2023/07/05 由陈凌定义
     * 声明不同座位的票价
     */
    private final int
            _price_business_seat, _price_first_class_seat, _price_second_class_seat, _price_no_seat;


    /**
     * 2023/07/04 由刘辉定义
     * 便于计算的一些变量
     */
    private final int _baseIndex_B;        // 商务座的 baseIndex
    private final int _baseIndex_F;        // 一等座的 baseIndex
    private final int _baseIndex_S;        // 二等座的 baseIndex
    private final int _sumOfSeats;         // 座位总数


    /**
     * 所有的座位的 map, 下标 映射 座位名称
     */
    private final Map<Integer, String> BUSINESS_SEAT_MAP = new HashMap<>();
    private final Map<String, Integer> BUSINESS_SEAT_MAP_REVERSE = new HashMap<>();
    private final Map<Integer, String> FIRST_CLASS_SEAT_MAP = new HashMap<>();
    private final Map<String, Integer> FIRST_CLASS_SEAT_MAP_REVERSE = new HashMap<>();
    private final Map<Integer, String> SECOND_CLASS_SEAT_MAP = new HashMap<>();
    private final Map<String, Integer> SECOND_CLASS_SEAT_MAP_REVERSE = new HashMap<>();


    /**
     * 由座位等级(商务\一等\二等)的枚举类型(实际是字符创), 映射到相应的座位表
     */
    private final Map<GSeriesSeatType, Map<Integer, String>> TYPE_MAP = new HashMap<>() {{
        put(GSeriesSeatType.BUSINESS_SEAT, BUSINESS_SEAT_MAP);
        put(GSeriesSeatType.FIRST_CLASS_SEAT, FIRST_CLASS_SEAT_MAP);
        put(GSeriesSeatType.SECOND_CLASS_SEAT, SECOND_CLASS_SEAT_MAP);
    }};


    /**
     * 2023/07/04 由 刘辉 修改
     * 添加了反表
     */
    private GSeriesSeatStrategy() {

        int counter = 0;

        _baseIndex_B = counter;
        for (String s : Arrays.asList("1车1A","1车1C","1车1F")) {
            BUSINESS_SEAT_MAP.put(counter, s);
            BUSINESS_SEAT_MAP_REVERSE.put(s, counter);
            ++counter;
        }

        _baseIndex_F = counter;
        for (String s : Arrays.asList("2车1A","2车1C","2车1D","2车1F","2车2A","2车2C","2车2D","2车2F","3车1A","3车1C","3车1D","3车1F")) {
            FIRST_CLASS_SEAT_MAP.put(counter, s);
            FIRST_CLASS_SEAT_MAP_REVERSE.put(s, counter);
            ++counter;
        }

        _baseIndex_S = counter;
        for (String s : Arrays.asList("4车1A","4车1B","4车1C","4车1D","4车2F","4车2A","4车2B","4车2C","4车2D","4车2F","4车3A","4车3B","4车3C","4车3D","4车3F")) {
            SECOND_CLASS_SEAT_MAP.put(counter, s);
            SECOND_CLASS_SEAT_MAP_REVERSE.put(s, counter);
            ++counter;
        }

        _sumOfSeats = counter;

        _price_business_seat = 300;
        _price_first_class_seat = 150;
        _price_second_class_seat = 100;
        _price_no_seat = 50;
    }


    /**
     * 2023/07/03 由 刘辉 实现
     * 用于实现 getLeftSeatCount() 的方法, 计算从 startStationIndex 到 endStationIndex, 且从 seatMap[][startSeat] 到 seatMap[][endSeat]的余票数量
     * @param startStationIndex 出发站点的编号
     * @param endStationIndex   到达站点的编号
     * @param seatMap           座位map
     * @param startSeat         闭区间
     * @param endSeat           闭区间
     * @return                  指定区间的余票数量
     */
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


    /**
     * 座位类型的枚举
     */
    public enum GSeriesSeatType implements SeatType {
        BUSINESS_SEAT("商务座"), FIRST_CLASS_SEAT("一等座"), SECOND_CLASS_SEAT("二等座"), NO_SEAT("无座");
        private final String text;
        GSeriesSeatType(String text){
            this.text=text;
        }
        public String getText() {
            return this.text;
        }

        /**
         * 用 String 获取枚举
         */
        public static GSeriesSeatType fromString(String text) {
            for (GSeriesSeatType b : GSeriesSeatType.values()) {
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
    @Override
    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, String seatType, boolean[][] seatMap) {
        if (seatType.equalsIgnoreCase("无座")) {
            return seatType;
        }
        // 获取座位类型 type 对应的 baseIndex 和 upperIndex 以便确定检索区间
        final int  baseIndex, upperIndex;

        GSeriesSeatType gSeriesSeatType = GSeriesSeatType.fromString(seatType);
        if (gSeriesSeatType == null) {
            return null;
        }
        switch (gSeriesSeatType) {
            case BUSINESS_SEAT:
                baseIndex = _baseIndex_B;
                upperIndex = _baseIndex_F - 1;
                break;
            case FIRST_CLASS_SEAT:
                baseIndex = _baseIndex_F;
                upperIndex = _baseIndex_S - 1;
                break;
           case SECOND_CLASS_SEAT:
                baseIndex = _baseIndex_S;
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
                return TYPE_MAP.get(gSeriesSeatType).get(seat);
            }
        }

        return null;
    }


    /**
     * 2023/07/03 由 刘辉 实现
     * 作用: 余票查询
     * 实现: 1. 一个座位有票, 当且仅当该座位从 startStation 到 endStation 之间都有票;
     *      2. 从车站 startStationIndex 到车站 startStationIndex + 1, 在 seatMap中对应 seaMap[startStationIndex][]
     *      3. 认为 seatMap[a][b] == false表示座位为空, 为 true 则座位被占有
     * @param startStationIndex 出发站点的编号
     * @param endStationIndex   到达站点的编号
     * @param seatMap           座位map
     * @return map, 由 座位等级枚举 映射到 相应的余票数量
     */
    public Map<String, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        Map<String, Integer> seatCount = new HashMap<>();

        int BUSINESS_SEAT_COUNT = _countLeftSeat(startStationIndex, endStationIndex, seatMap, _baseIndex_B, _baseIndex_F - 1);
        int FIRST_CLASS_SEAT_COUNT = _countLeftSeat(startStationIndex, endStationIndex, seatMap, _baseIndex_F, _baseIndex_S - 1);
        int SECOND_CLASS_SEAT_COUNT = _countLeftSeat(startStationIndex, endStationIndex, seatMap, _baseIndex_S, _sumOfSeats - 1);

        seatCount.put(GSeriesSeatType.BUSINESS_SEAT.getText(), BUSINESS_SEAT_COUNT);
        seatCount.put(GSeriesSeatType.FIRST_CLASS_SEAT.getText(), FIRST_CLASS_SEAT_COUNT);
        seatCount.put(GSeriesSeatType.SECOND_CLASS_SEAT.getText(), SECOND_CLASS_SEAT_COUNT);

        return seatCount;
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

        Integer seatIndex = BUSINESS_SEAT_MAP_REVERSE.get(seat);
        if (seatIndex == null) {
            seatIndex = FIRST_CLASS_SEAT_MAP_REVERSE.get(seat);
        }
        if (seatIndex == null) {
            seatIndex = SECOND_CLASS_SEAT_MAP_REVERSE.get(seat);
        }
        if (seatIndex == null) {
            throw new NoSuchElementException("No such seat! Error occurs in GSeriesSeatStrategy.java updateSeatMap()");
        }

        for (int station = startStationIndex; station < endStationIndex; ++station) {
            seatMap[station][seatIndex] = seatStatus;
        }

        return seatMap;
    }

    public Integer getSeatTypePrice(String seatType) {
        if (seatType.equalsIgnoreCase("商务座")) {
            return _price_business_seat;
        }
        if (seatType.equalsIgnoreCase("一等座")) {
            return _price_first_class_seat;
        }
        if (seatType.equalsIgnoreCase("二等座")) {
            return _price_second_class_seat;
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
        price = BUSINESS_SEAT_MAP_REVERSE.get(seat);
        if (price != null) {
            return _price_business_seat;
        }
        price = FIRST_CLASS_SEAT_MAP_REVERSE.get(seat);
        if (price != null) {
            return _price_first_class_seat;
        }
        price = SECOND_CLASS_SEAT_MAP_REVERSE.get(seat);
        if (price != null) {
            return _price_second_class_seat;
        }

        return null;
    }


    /**
     * 2023/07/04 由刘辉修改
     * 作用: 初始化座位map, false为未售
     * 使用: addTrain 中被调用
     * @param stationCount 车次途径的车站数量
     */
    public boolean[][] initSeatMap(int stationCount) {
        assert BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size() + SECOND_CLASS_SEAT_MAP.size() == _sumOfSeats;
        return new boolean[stationCount - 1][_sumOfSeats];
    }
}
