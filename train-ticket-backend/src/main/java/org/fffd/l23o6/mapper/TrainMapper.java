package org.fffd.l23o6.mapper;

import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE) // TODO-cl
public interface TrainMapper {
    TrainMapper INSTANCE = Mappers.getMapper(TrainMapper.class);

    @Mapping(source = "TrainEntity.trainType.text", target = "trainType")
    AdminTrainVO toAdminTrainVO(TrainEntity TrainEntity);
    
    TrainVO toTrainVO(TrainEntity TrainEntity);
}

