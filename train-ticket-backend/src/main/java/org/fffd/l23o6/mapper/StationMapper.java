package org.fffd.l23o6.mapper;

import org.fffd.l23o6.pojo.entity.StationEntity;
import org.fffd.l23o6.pojo.vo.station.StationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StationMapper {
    StationMapper INSTANCE = Mappers.getMapper(StationMapper.class);

    StationVO toStationVO(StationEntity stationEntity);
}