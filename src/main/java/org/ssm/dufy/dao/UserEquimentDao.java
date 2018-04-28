package org.ssm.dufy.dao;


import java.util.List;

import org.ssm.dufy.entity.UserEquimentInfo;


public interface UserEquimentDao {
    public List<UserEquimentInfo> findUserEquiment(String userId);
}