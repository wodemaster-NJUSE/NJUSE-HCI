package org.fffd.l23o6.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.StationDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.mapper.StationMapper;
import org.fffd.l23o6.pojo.entity.StationEntity;
import org.fffd.l23o6.pojo.vo.station.StationVO;
import org.fffd.l23o6.service.StationService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService{
    private final StationDao stationDao;
    @Override
    public StationVO getStation(Long stationId){
        return StationMapper.INSTANCE.toStationVO(stationDao.findById(stationId).get());
    }
    @Override
    public List<StationVO> listStations(){
        return stationDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(StationMapper.INSTANCE::toStationVO).collect(Collectors.toList());
    }
    @Override
    public void addStation(String name){
        StationEntity entity = stationDao.findByName(name);
        if(entity!=null){
            throw new BizException(BizError.STATIONNAME_EXISTS);
        }
        stationDao.save(StationEntity.builder().name(name).build());
    }
    @Override
    public void editStation(Long id, String name){
        StationEntity entity = stationDao.findById(id).get();
        entity.setName(name);
        stationDao.save(entity);
    }
}
