package org.ssm.dufy.dao;


import java.util.List;

import org.ssm.dufy.entity.UserInfo;


public interface UserDao {
    public List<UserInfo> findUserInfo(UserInfo userInfo);
    public void addEntity(UserInfo user);
    public void editEntity(UserInfo userInfo);
    public void deleteByNames(UserInfo userInfo);
    public String selectByPrimaryKey(String id);
}