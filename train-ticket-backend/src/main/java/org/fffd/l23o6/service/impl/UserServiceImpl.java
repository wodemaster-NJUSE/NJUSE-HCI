package org.fffd.l23o6.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param name     名字
     * @param idn      idn
     * @param phone    电话
     * @param type     用户类型
     */
    @Override
    public void register(String username, String password, String name, String idn, String phone, String type) {
        UserEntity user = userDao.findByUsername(username);

        if (user != null) {
            throw new BizException(BizError.USERNAME_EXISTS);
        }

        userDao.save(UserEntity.builder().username(username).password(BCrypt.hashpw(password))
                .name(name).idn(idn).phone(phone).type(type).MileagePoints(0).build());
    }

    /**
     * 根据 username 查找用户
     * @param username 用户名
     * @return         用户实体
     */
    @Override
    public UserEntity findByUserName(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public void login(String username, String password) {
        UserEntity user = userDao.findByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw new BizException(BizError.INVALID_CREDENTIAL);
        }
    }

    /**
     * 编辑用户信息
     * @param username 用户名
     * @param name     名字
     * @param idn      idn
     * @param phone    电话号码
     * @param type     用户类型
     */
    @Override
    public void editInfo(String username, String name, String idn, String phone, String type){
        UserEntity user = userDao.findByUsername(username);
        if(user == null){
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "用户不存在");
        }
        userDao.save(user.setIdn(idn).setName(name).setPhone(phone).setType(type));
    }
}