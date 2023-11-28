package org.fffd.l23o6.service.impl;


import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.service.TrainService;

import java.util.Date;
import java.util.List;

// TODO: 2023/5/26
// add interface and implement it
public class TrainServiceImpl implements TrainService {
    private final TrainDao trainDao;

    public TrainServiceImpl(TrainDao trainDao) {
        this.trainDao = trainDao;
    }

    @Override
    public TrainDetailVO getTrain(Long trainId) {

        return TrainMapper.INSTANCE.toTrainVO(trainDao.findById(trainId).get());
    }

    @Override
    public List<TrainVO> listTrains(Long startStationId, Long endStationId, String date) {
        return TrainMapper.INSTANCE.toTrainVOs(trainDao.findAll());
    }

    @Override
    public List<AdminTrainVO> listTrainsAdmin() {
        return TrainMapper.INSTANCE.toTrainAdminVOs(trainDao.findAll());
    }

    @Override
    public void addTrain(String name, Long routeId, String type, String date, List<Date> arrivalTimes, List<Date> departureTimes) {
        trainDao.save(TrainEntity.builder().name(name).build());
    }
}
