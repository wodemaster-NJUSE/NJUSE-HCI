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

    /**
     * 根据车站的 id 获取该车站的 VO
     * @param stationId 车站 id
     * @return          车站 VO
     */
    @Override
    public StationVO getStation(Long stationId){
        return StationMapper.INSTANCE.toStationVO(stationDao.findById(stationId).get());
    }

    /**
     * 列出车站
     * @return 车站的 VO 列表
     */
    @Override
    public List<StationVO> listStations(){
        return stationDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(StationMapper.INSTANCE::toStationVO).collect(Collectors.toList());
    }

    /**
     * 添加车站
     * @param name 车站名
     */
    @Override
    public void addStation(String name){
        StationEntity entity = stationDao.findByName(name);
        if(entity!=null){
            throw new BizException(BizError.STATIONNAME_EXISTS);
        }
        stationDao.save(StationEntity.builder().name(name).build());
    }

    /**
     * 编辑 指定id 对应的车站的信息
     * @param id   车站id
     * @param name 车站名
     */
    @Override
    public void editStation(Long id, String name){
        StationEntity entity = stationDao.findById(id).get();
        entity.setName(name);
        stationDao.save(entity);
    }
}
