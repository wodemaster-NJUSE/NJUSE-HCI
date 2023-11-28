package org.fffd.l23o6.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.mapper.impl.ToTrainVoMapperImpl;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.service.TrainService;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    /**
     * 根据车次的 id, 获取该车次的 VO
     * @param trainId 车次 id
     * @return        车次 VO
     */
    @Override
    public TrainDetailVO getTrain(Long trainId) {
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        return TrainDetailVO.builder().id(trainId).date(train.getDate()).name(train.getName())
                .stationIds(route.getStationIds()).arrivalTimes(train.getArrivalTimes())
                .departureTimes(train.getDepartureTimes()).extraInfos(train.getExtraInfos()).build();
    }

    /**
     * 列出指定条件下可行的车次
     * @param startStationId 通过起始站点id、
     * @param endStationId   终止站点id、来
     * @param date           日期
     * @return               所有可行车次的 VO 组成的列表
     */
    @Override
    public List<TrainVO> listTrains(Long startStationId, Long endStationId, String date) {
        // fixed TODO
        // 现根据date通过trains筛选出符合的trainentity，然后根据trainentity里的routeid得到对应线路，
        // 如果线路里包含起止站点，则将该trainentity收集，最后转为trainvo

        // 获取所有车
        List<TrainEntity> allTrains = trainDao.findAll();

        // 选出日期正确的车
        List<TrainEntity> selectedTrainsByDate = new ArrayList<>();
        for (TrainEntity trainEntity : allTrains) {
            if(Objects.equals(trainEntity.getDate(), date)){
                selectedTrainsByDate.add(trainEntity);
            }
        }

        List<TrainEntity> selectedTrainsByRoute = new ArrayList<>();
        for( TrainEntity trainEntity : selectedTrainsByDate) {
            RouteEntity routeEntity = routeDao.findById(trainEntity.getRouteId()).get();
            if(routeEntity.getStationIds().contains(startStationId) &&
                    routeEntity.getStationIds().contains(endStationId)) {
                selectedTrainsByRoute.add(trainEntity);
            }
        }

        List<TrainVO> trainVOList = new ArrayList<>();
        for (TrainEntity trainEntity : selectedTrainsByRoute) {
            RouteEntity route = routeDao.findById(trainEntity.getRouteId()).get();
            long startStationIndex = route.getStationIds().indexOf(startStationId);
            long endStationIndex = route.getStationIds().indexOf(endStationId);

            trainVOList.add(ToTrainVoMapperImpl.instance.toTrainVO(trainEntity, startStationIndex, endStationIndex, startStationId, endStationId));
        }

        return trainVOList;
    }

    /**
     * 列出所有 AdminTrain
     * @return AdminTrainVO 组成的列表
     */
    @Override
    public List<AdminTrainVO> listTrainsAdmin() {
        return trainDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(TrainMapper.INSTANCE::toAdminTrainVO).collect(Collectors.toList());
    }

    /**
     * 添加车次
     * @param name           train 的名称
     * @param routeId        train 的的路线的 id
     * @param type           train 的类型
     * @param date           train 的日期
     * @param arrivalTimes   train 的出发时间
     * @param departureTimes train 的到达时间
     */
    @Override
    public void addTrain(String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
            List<Date> departureTimes) {
        TrainEntity trainEntity = TrainEntity.builder().name(name).routeId(routeId).trainType(type)
                .date(date).arrivalTimes(arrivalTimes).departureTimes(departureTimes).build();
        RouteEntity route = routeDao.findById(routeId).get();
        if (route.getStationIds().size() != trainEntity.getArrivalTimes().size()
                || route.getStationIds().size() != trainEntity.getDepartureTimes().size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }
        trainEntity.setExtraInfos(new ArrayList<String>(Collections.nCopies(route.getStationIds().size(), "预计正点")));

        switch (trainEntity.getTrainType()) {
            case HIGH_SPEED:
                trainEntity.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
            case NORMAL_SPEED:
                trainEntity.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
        }
        trainDao.save(trainEntity);
    }

    /**
     * 编辑 train 的信息
     * @param id             train 的 id
     * @param name           train 的名称
     * @param routeId        train 的的路线的 id
     * @param type           train 的类型
     * @param date           train 的日期
     * @param arrivalTimes   train 的出发时间
     * @param departureTimes train 的到达时间
     */
    @Override
    public void changeTrain(Long id, String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
                            List<Date> departureTimes) {
        //fixed TODO: edit train info, please refer to `addTrain` above
        TrainEntity trainEntity = trainDao.findById(id).get();
        trainEntity.setName(name);
        trainEntity.setRouteId(routeId);
        trainEntity.setTrainType(type);
        trainEntity.setDate(date);
        trainEntity.setArrivalTimes(arrivalTimes);
        trainEntity.setDepartureTimes(departureTimes);

        trainDao.save(trainEntity);
    }

    /**
     * 删除指定 id 对应的 train
     * @param id train 的 id
     */
    @Override
    public void deleteTrain(Long id) {
        trainDao.deleteById(id);
    }
}
