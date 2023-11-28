package org.fffd.l23o6.mapper;

import org.fffd.l23o6.mapper.impl.ToTrainVoMapperImpl;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.train.TrainVO;

public interface ToTrainVoMapper {
    public TrainVO toTrainVO(TrainEntity trainEntity, long startStationIndex, long endStationIndex,
                             long startStationId, long endStationId);
}
