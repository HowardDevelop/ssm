package org.ssm.dufy.dao;

import java.util.List;

import org.ssm.dufy.entity.EquimentInfo;


public interface EquimentDao {
    public void addEntity(EquimentInfo equimentInfo);

    public void editEntity(EquimentInfo equimentInfo);

    public void deleteByNames(EquimentInfo equimentInfo);
    
    public List<EquimentInfo> findEquimentInfo(EquimentInfo equimentInfo);

    public EquimentInfo selectByPrimaryKey(String id);
}
