package org.ssm.dufy.service;

import org.ssm.dufy.entity.EquimentInfo;
import org.ssm.dufy.entity.UserInfo;

public interface UserService {
    /**
     * ��½��֤
     * 
     * @param name
     * @param password
     * @return
     * @throws Exception
     */
    public String login(String name, String password) throws Exception;

    /**
     * ��ѯ�û���Ϣ
     * 
     * @param name
     *            ,password
     * @return
     * @throws Exception
     */
    public String selectUserMsg(String name) throws Exception;

    /**
     * ����û���Ϣ
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String addUserMsg(UserInfo user) throws Exception;

    /**
     * �����û���Ϣ
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String updateUserMsg(String userName, UserInfo user)
            throws Exception;

    /**
     * ɾ���û���Ϣ
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String deleteUserMsg(String name) throws Exception;

    /**
     * ��ѯ�豸��Ϣ
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public String selectEquimentMsg(String equimentName) throws Exception;

    /**
     * ����豸��Ϣ
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String addEquimentMsg(EquimentInfo equimentInfo) throws Exception;

    /**
     * �����豸��Ϣ
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String updateEquimentMsg(String equimentName,
            EquimentInfo equimentInfo) throws Exception;

    /**
     * ɾ���豸��Ϣ
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public String deleteEquimentMsg(String equimentName) throws Exception;

    /**
     * ��ȡ�û��豸��ϵ
     * 
     * @param userName
     * @return
     */
    public String getUserEquiment(String userName) throws Exception;

}
