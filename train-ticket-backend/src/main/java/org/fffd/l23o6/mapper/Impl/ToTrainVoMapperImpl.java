package org.fffd.l23o6.mapper.impl;

import org.fffd.l23o6.mapper.ToTrainVoMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.TicketInfo;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.TrainSeatStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToTrainVoMapperImpl implements ToTrainVoMapper {
    public static ToTrainVoMapperImpl instance = new ToTrainVoMapperImpl();
    private ToTrainVoMapperImpl() {};
    public TrainVO toTrainVO(TrainEntity trainEntity, long startStationIndex, long endStationIndex,
                             long startStationId, long endStationId) {
        if ( trainEntity == null ) {
            return null;
        }

        String trainType = null;
        Long id = null;
        String name = null;

        trainType = trainEntityTrainTypeText( trainEntity );
        id = trainEntity.getId();
        name = trainEntity.getName();

        Date arrivalTime = trainEntity.getArrivalTimes().get((int) endStationIndex);
        Date departureTime = trainEntity.getDepartureTimes().get((int) startStationIndex);

        TrainSeatStrategy trainSeatStrategy = null;
        switch (trainEntity.getTrainType()) {
            case HIGH_SPEED:
                trainSeatStrategy = GSeriesSeatStrategy.INSTANCE;
                break;
            case NORMAL_SPEED:
                trainSeatStrategy = KSeriesSeatStrategy.INSTANCE;
                break;
        }

        // 获取 TicketInfo
        List<TicketInfo> ticketInfoList = new ArrayList<>();
        Map<String, Integer> seatAndLeftSeatCount = trainSeatStrategy.getLeftSeatCount(
                (int) startStationIndex,
                (int) endStationIndex,
                trainEntity.getSeats());

        for (Map.Entry<String, Integer> entry : seatAndLeftSeatCount.entrySet()) {
            String seatType = entry.getKey();
            Integer price = trainSeatStrategy.getSeatTypePrice(seatType);
            ticketInfoList.add(new TicketInfo(seatType, entry.getValue(), price));
        }

        return new TrainVO( id, name, trainType, startStationId, endStationId, departureTime, arrivalTime, ticketInfoList );
    }

    private String trainEntityTrainTypeText(TrainEntity trainEntity) {
        if ( trainEntity == null ) {
            return null;
        }
        TrainType trainType = trainEntity.getTrainType();
        if ( trainType == null ) {
            return null;
        }
        String text = trainType.getText();
        if ( text == null ) {
            return null;
        }
        return text;
    }
}
