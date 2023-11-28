package org.fffd.l23o6.mapper;

import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.vo.user.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserVO toUserVO(UserEntity userEntity);
}