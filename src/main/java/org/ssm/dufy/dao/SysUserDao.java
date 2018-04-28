package org.ssm.dufy.dao;


import java.util.List;

import org.ssm.dufy.entity.SysUserInfo;



public interface SysUserDao {
    public List<SysUserInfo> findUserInfo(SysUserInfo ysUserInfo);
}