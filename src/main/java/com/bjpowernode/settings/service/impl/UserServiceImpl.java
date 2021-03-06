package com.bjpowernode.settings.service.impl;

import com.bjpowernode.exception.LoginException;
import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:任赵辉
 * 2019/6/19
 */
public class UserServiceImpl implements UserService {
private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userDao.login(map);
        if (user == null){
            throw new LoginException("账号密码错误，请重新输入！");
        }
        String expireTime = user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效，请联系管理员！");
        }
        if ("0".equals(user.getLockState())){
            throw new LoginException("账号已锁定，请联系管理员！");
        }
        if (!user.getAllowIps().contains(ip)){
            throw new LoginException("Ip地址访问受限！");
        }
        return user;
    }
}
