package org.ssm.dufy.service;

import org.ssm.dufy.entity.EquimentInfo;
import org.ssm.dufy.entity.UserInfo;

public interface UserService {
    /**
     * 登陆验证
     * 
     * @param name
     * @param password
     * @return
     * @throws Exception
     */
    public String login(String name, String password) throws Exception;

    /**
     * 查询用户信息
     * 
     * @param name
     *            ,password
     * @return
     * @throws Exception
     */
    public String selectUserMsg(String name) throws Exception;

    /**
     * 添加用户信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String addUserMsg(UserInfo user) throws Exception;

    /**
     * 更新用户信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String updateUserMsg(String userName, UserInfo user)
            throws Exception;

    /**
     * 删除用户信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String deleteUserMsg(String name) throws Exception;

    /**
     * 查询设备信息
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public String selectEquimentMsg(String equimentName) throws Exception;

    /**
     * 添加设备信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String addEquimentMsg(EquimentInfo equimentInfo) throws Exception;

    /**
     * 更新设备信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String updateEquimentMsg(String equimentName,
            EquimentInfo equimentInfo) throws Exception;

    /**
     * 删除设备信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String deleteEquimentMsg(String equimentName) throws Exception;

    /**
     * 获取用户设备关系
     * 
     * @param userName
     * @return
     */
    public String getUserEquiment(String userName) throws Exception;

}
