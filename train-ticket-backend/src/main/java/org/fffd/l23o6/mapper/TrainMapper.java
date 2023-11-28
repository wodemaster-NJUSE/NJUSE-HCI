package org.fffd.l23o6.mapper;

import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TrainMapper {
    TrainMapper INSTANCE = Mappers.getMapper(TrainMapper.class);

    TrainDetailVO toTrainVO(TrainEntity trainEntity);

    List<TrainVO> toTrainVOs(List<TrainEntity> all);

    List<AdminTrainVO> toTrainAdminVOs(List<TrainEntity> all);
    // Something missing here? But what is it?
    // Just take a look at its cousins.
    // TODO: 2023/5/26
}

