package org.ssm.dufy.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssm.dufy.dao.EquimentDao;
import org.ssm.dufy.dao.UserDao;
import org.ssm.dufy.dao.UserEquimentDao;
import org.ssm.dufy.entity.EquimentInfo;
import org.ssm.dufy.entity.UserEquimentInfo;
import org.ssm.dufy.entity.UserInfo;
import org.ssm.dufy.service.UserService;
import org.ssm.dufy.util.Encrypt;
import org.ssm.dufy.util.JsonUtils;
import org.ssm.dufy.util.ResponseDto;
import org.ssm.dufy.util.StringUtil;



@Service("userService")
public class UserServiceImpl implements UserService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public UserDao userDao;
    @Autowired
    public UserEquimentDao userEquimentDao;
    @Autowired
    public EquimentDao equimentDao;
 // �������
    private Encrypt encrypt = new Encrypt();

    /**
     * ��½��֤
     * 
     * @param name
     * @param password
     * @return
     * @throws Exception
     */
    public String login(String name, String password) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        UserInfo userInfo = new UserInfo();
        try {
            if (!StringUtil.isEmpty(name) && !StringUtil.isEmpty(password)) {
                userInfo.setName(name);
                List<UserInfo> list = userDao.findUserInfo(userInfo);
                if (null != list && list.size() > 0) {
                    if (password.equals(encrypt.decoder(list.get(0).getPassword()))) {
                        responseDto.setErrcode("0");
                        responseDto.setErrmsg("��֤��½�ɹ���");
                        responseDto.setResponseObject(list.get(0));
                        return JsonUtils.bean2json(responseDto);
                    }else {
                        responseDto.setErrcode("1");
                        responseDto.setErrmsg("�������");
                        return JsonUtils.bean2json(responseDto);
                    }
                }
                responseDto.setErrcode("1");
                responseDto.setErrmsg("�û�������");
            }
        } catch (Exception e) {
            // TODO: handle exception
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��֤��½��Ϣ�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * ��ѯ�û���Ϣ
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public String selectUserMsg(String userName) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // ����������check
            if (!checkUserName(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setName(userName);
            List<UserInfo> list = userDao.findUserInfo(userInfo);
            if (null == list || list.size() == 0) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg(userName + "�û�������");
                return JsonUtils.bean2json(responseDto);
            }
            userInfo = list.get(0);
            responseDto.setErrcode("0");// У��ɹ�
            responseDto.setErrmsg("��ѯ�ɹ�");
            responseDto.setResponseObject(userInfo);// �����û���Ϣ
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("��ѯ�û���Ϣ�쳣");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѯ�û���Ϣ�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * �����û�
     * 
     * @param UserInfo
     * @return
     */
    public String addUserMsg(UserInfo user) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // ����������check
            if (!checkUserParameter(user, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // �ж��Ƿ��ѱ�ע��
            if (checkUserColumn(user.getName(), "�û���", "NAME", responseDto)
                    && checkUserColumn(user.getPhone(), "�ֻ���", "PHONE",
                            responseDto)) {
            }
            addOther(user);// ���javabeanû�е������ֶ�
            user.setPassword(encrypt.encoder(user.getPassword()));
            userDao.addEntity(user);
            responseDto.setErrcode("0");// У���� ,1��ʧ��,0�ǳɹ�
            responseDto.setErrmsg("��ӳɹ�");
            responseDto.setResponseObject(user);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("����û��쳣");
            responseDto.setErrcode("1");// У���� ,1��ʧ��,0�ǳɹ�
            responseDto.setErrmsg("����û���Ϣ�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * �����û���Ϣ
     * 
     * @param userName
     * @param userInfo
     * @return
     */
    public String updateUserMsg(String userName, UserInfo userInfo)
            throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // ����������check
            if (!checkUserParameterUpdate(userInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // ָ���û����ݴ���check
            if (!checkUserIdByDB(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // �ж��Ƿ��ѱ�ע��
            if (checkUserColumn(userInfo.getName(), "�û���", "NAME", userName,
                    responseDto)
                    && checkUserColumn(userInfo.getPhone(), "�ֻ���", "PHONE",
                            userName, responseDto)) {
                userInfo.setId(responseDto.getResponseObject().toString());
                userDao.editEntity(userInfo);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("���³ɹ�");
                // responseDto.setResponseObject(userInfo);
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("�����û��쳣");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("�����û���Ϣ�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * ɾ���û�
     * 
     * @param name
     * @return
     */
    public String deleteUserMsg(String name) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        UserInfo userInfo = new UserInfo();
        try {
            // ����������check
            if (!checkUserName(name, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // ָ���û����ݴ���check
            if (!checkUserNameByDB(name, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            userInfo.setId(responseDto.getResponseObject().toString());
            userInfo.setDeleteFlag("1");
            userDao.editEntity(userInfo);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("ɾ���ɹ�");
            // responseDto.setResponseObject(userInfo);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("ɾ���û��쳣");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("ɾ���û��쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * ��ȡ�豸��Ϣ
     * 
     * @param equimentName
     */
    public String selectEquimentMsg(String equimentName) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        EquimentInfo equimentInfo = new EquimentInfo();
        try {
            // ����������check
            if (!checkEquimentName(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            equimentInfo.setEquimentName(equimentName);
            List<EquimentInfo> list = equimentDao
                    .findEquimentInfo(equimentInfo);
            if (null == list || list.size() == 0) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg(equimentName + "�豸������");
                return JsonUtils.bean2json(responseDto);
            }
            equimentInfo = list.get(0);
            responseDto.setErrcode("0");// У��ɹ�
            responseDto.setErrmsg("��ѯ�ɹ�");
            responseDto.setResponseObject(equimentInfo);// �����û���Ϣ
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("��ѯ�豸��Ϣ�쳣");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѯ�豸��Ϣ�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * �����豸
     * 
     * @param equimentInfo
     */
    public String addEquimentMsg(EquimentInfo equimentInfo) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // ����������check
            if (!checkEquimentName(equimentInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // �ж��Ƿ��ѱ�ע��
            if (checkEquimentColumn(equimentInfo.getEquimentName(), "�豸����",
                    "EQUIMENT_NAME", responseDto)
                    && checkEquimentColumn(equimentInfo.getModubusAdress(),
                            "�豸modubus��ַ", "MODUBUS_ADDRESS", responseDto)) {

                addOther(equimentInfo);// ���javabeanû�е������ֶ�
                equimentDao.addEntity(equimentInfo);
                responseDto.setErrcode("0");// У���� ,1��ʧ��,0�ǳɹ�
                responseDto.setErrmsg("��ӳɹ�");
                // responseDto.setResponseObject(equimentInfo);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("����豸�쳣");
            responseDto.setErrcode("1");// У���� ,1��ʧ��,0�ǳɹ�
            responseDto.setErrmsg("����豸�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }


    /**
     * �����豸��Ϣ
     * 
     * @param equimentInfo
     * @param equimentName
     */
    public String updateEquimentMsg(String equimentName,
            EquimentInfo equimentInfo) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // ����������check
            if (!checkEquimentParameterUpdate(equimentInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // ָ���û����ݴ���check
            if (!checkEquimentIdByDB(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // �ж��Ƿ��ѱ�ע��
            if (checkEquimentColumn(equimentInfo.getEquimentName(), "�豸����",
                    "EQUIMENT_NAME",equimentName, responseDto)
                    && checkEquimentColumn(equimentInfo.getModubusAdress(),
                            "�豸��ַ", "MODUBUS_ADDRESS", equimentName,responseDto)) {
                equimentInfo.setId(responseDto.getResponseObject().toString());
                equimentDao.editEntity(equimentInfo);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("���³ɹ�");
                // responseDto.setResponseObject(equimentInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("�����豸�쳣");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("�����豸�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * ɾ���豸��Ϣ
     */
    public String deleteEquimentMsg(String equimentName) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // ����������check
            if (!checkEquimentName(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // ָ���û����ݴ���check
            if (!checkEquimentNameByDB(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            EquimentInfo equimentInfo = new EquimentInfo();
            equimentInfo.setId(responseDto.getResponseObject().toString());
            equimentInfo.setDeleteFlag("1");
            equimentDao.editEntity(equimentInfo);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("ɾ���ɹ�");
            // responseDto.setResponseObject(equimentInfo);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("ɾ���豸�쳣");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("ɾ���豸�쳣");
        }
        return JsonUtils.bean2json(responseDto);
    }
    /**
     * ��ȡ�û��豸��Ϣ
     * 
     * @param accessToken
     * @param userName
     * @return
     */
    public String getUserEquiment(String userName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // ����������check
            if (!checkUserName(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            String[] roleName = getEquimentIdByUserName(userName, responseDto);
            if (roleName == null) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("���û���δӵ�пɲ�ѯ�豸����û�������");
            } else {
                responseDto.setErrcode("0");
                responseDto.setErrmsg("��ѯ�ɹ�");
                responseDto.setResponseObject(roleName);// �����û���Ϣ
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("��ѯ�û��豸��Ϣ�쳣");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѯ�û��豸��Ϣ�쳣");
        }

        return JsonUtils.bean2json(responseDto);
    }
    /**
     * ͨ���û�����ȡ�豸��
     * 
     * @param userName
     * @param responseDto
     * @return
     */
    private String[] getEquimentIdByUserName(String userName, ResponseDto responseDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userName);
        List<UserInfo> users = userDao.findUserInfo(userInfo);// �û���Ϣ��ѯ
        if (users == null || users.size() == 0)
            return null;

        String userId = users.get(0).getId();
        List<UserEquimentInfo> userequiments = userEquimentDao.findUserEquiment(userId);
        if (userequiments == null || userequiments.size() == 0) {
            return null;
        }
        String[] ids = new String[userequiments.size()];
        String[] equimentName = new String[userequiments.size()];
        for (int i = 0; i < userequiments.size(); i++) {
            ids[i] = userequiments.get(i).getEquimentId();
            EquimentInfo equimentInfo = equimentDao.selectByPrimaryKey(ids[i]);
            equimentName[i] = equimentInfo.getEquimentName();
        }
        return equimentName;
    }
    /**
     * ��֤�豸�����Ƿ����
     * 
     * @param equimentName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentNameByDB(String equimentName,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        EquimentInfo equimentInfo = new EquimentInfo();
        equimentInfo.setEquimentName(equimentName);
        List<EquimentInfo> equiInfos = equimentDao
                .findEquimentInfo(equimentInfo);
        if (null == equiInfos || equiInfos.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("�豸(" + equimentName + ")������");
            return false;
        }
        String Id = equiInfos.get(0).getId();
        responseDto.setResponseObject(Id);
        return true;
    }

    /**
     * ���beanû�е������ֶ�
     * 
     * @param apiUserFormMap
     */
    private void addOther(UserInfo user) {
        user.setDeleteFlag("0");
    }

    /**
     * ��֤����ʱ���ֲ���Ψһ
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc,
            String columnName, ResponseDto responseDto) {
        return this.checkUserColumn(columnValue, columnDisc, columnName, 0,
                responseDto);
    }

    /**
     * ��֤����ʱ���ֲ���Ψһ
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc,
            String columnName, int i, ResponseDto responseDto) {
        UserInfo userInfo = new UserInfo();
        if ("NAME".equals(columnName)) {
            userInfo.setName(columnValue);
        }
        if ("PASSWORD".equals(columnName)) {
            userInfo.setPassword(columnValue);
        }
        if ("PHONE".equals(columnName)) {
            userInfo.setPassword(columnValue);
        }
        List<UserInfo> userInfos = userDao.findUserInfo(userInfo);
        if (userInfos != null && userInfos.size() > 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + columnDisc
                    + "��" + columnValue + "���ѱ�ʹ��");
            return false;
        }
        return true;
    }
   
    /**
     * ��鴴�������û��Ĳ���
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserParameter(UserInfo userInfo,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        if (!checkUserInfo(userInfo, responseDto)) {
            return false;
        }

        if (!checkNamePassword(userInfo, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * ��֤�û��������벻��Ϊ��
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkNamePassword(UserInfo userInfo, ResponseDto responseDto) {
        return this.checkNamePassword(userInfo, 0, responseDto);
    }

    /**
     * ��֤�û��������벻��Ϊ��
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkNamePassword(UserInfo userInfo, int i,
            ResponseDto responseDto) {
        if (userInfo.getName().isEmpty()) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�û���Ϊ��");
            return false;
        }
        if (userInfo.getPassword().isEmpty()) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "����Ϊ��");
            return false;
        }if(userInfo.getPhone().isEmpty()) {
        	responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�ֻ���Ϊ��");
            return false;
        }
        return true;
    }

    /**
     * ��֤���������û���Ϣ��Ϊ��
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserInfo(UserInfo userInfo, ResponseDto responseDto) {
        // TODO Auto-generated method stub
        return this.checkUserInfo(userInfo, 0, responseDto);
    }

    /**
     * ��֤���������û���Ϣ��Ϊ��
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserInfo(UserInfo userInfo, int i,
            ResponseDto responseDto) {
        if (userInfo == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�û���ϢΪ��");
            return false;
        }

        return true;
    }

    /**
     * ��֤����ʱ���ֲ���Ψһ
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc,
            String columnName, String userName, ResponseDto responseDto) {
        return this.checkUserColumn(columnValue, columnDisc, columnName,
                userName, 0, responseDto);
    }

    /**
     * ��֤����ʱ���ֲ���Ψһ
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc,
            String columnName, String userName, int i, ResponseDto responseDto) {
        UserInfo userInfo = new UserInfo();
        if ("NAME".equals(columnName)) {
            userInfo.setName(columnValue);
        }
        if ("PHONE".equals(columnName)) {
            userInfo.setPhone(columnValue);
        }
        List<UserInfo> userInfos = userDao.findUserInfo(userInfo);
        if (userInfos != null && userInfos.size() > 0) {
                    if (userName.equals(userInfos.get(0).getName())) {
                        return true;
            }
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + columnDisc
                    + "��" + columnValue + "���ѱ�ʹ��");
            return false;
        }
        return true;
    }

    /**
     * ��֤����ʱuserId����
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserIdByDB(String userName, ResponseDto responseDto) {
        if (userName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ���û�");
            return false;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userName);
        List<UserInfo> lists = userDao.findUserInfo(userInfo);
        if (lists == null || lists.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ����û�������");
            return false;
        }
        responseDto.setResponseObject(lists.get(0).getId());
        return true;
    }

    /**
     * �����û���Ϣ�Ĳ������
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserParameterUpdate(UserInfo userInfo,
            ResponseDto responseDto) {
        if (!checkUserParameter(userInfo, responseDto)) {
            return false;
        }
        if (!checkUserId(userInfo.getName(), responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * ɾ���û��Ĳ������
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserId(String userName, ResponseDto responseDto) {
        return this.checkUserId(userName, 0, responseDto);
    }

    /**
     * ɾ���û��Ĳ������
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserId(String userName, int i, ResponseDto responseDto) {
        if (userName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ��" + (i == 0 ? "" : "��" + i + "��") + "�û�");
            return false;
        }
        return true;
    }

    /**
     * ɾ���û��Ĳ������
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserName(String userName, ResponseDto responseDto) {
        return this.checkUserName(userName, 0, responseDto);
    }

    /**
     * ɾ���û��Ĳ������
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserName(String userName, int i,
            ResponseDto responseDto) {
        if (userName.isEmpty()) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ��" + (i == 0 ? "" : "��" + i + "��") + "�û�");
            return false;
        }
        return true;
    }

    /**
     * ����û��Ƿ����
     * 
     * @param userName
     * @param i
     * @param responseDto
     * @return
     */
    private boolean checkUserNameByDB(String userName, ResponseDto responseDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userName);
        List<UserInfo> userInfos = userDao.findUserInfo(userInfo);// �û���Ϣ��ѯ
        if (userInfos == null || userInfos.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("�û�(" + userName + ")������");
            return false;
        }
        String userId = userInfos.get(0).getId();
        responseDto.setResponseObject(userId);
        return true;
    }

    /**
     * ɾ���豸�Ĳ������
     * 
     * @param equimentName
     * @param responseDto
     * @return
     */

    private boolean checkEquimentName(String equimentName,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        return this.checkEquimentName(equimentName, 0, responseDto);
    }

    /**
     * ɾ���豸�Ĳ������
     * 
     * @param equimentName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentName(java.lang.String equimentName, int i,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        if (StringUtil.isEmpty(equimentName)) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ��" + (i == 0 ? "" : "��" + i + "��") + "�豸");
            return false;
        }
        return true;
    }
    /**
     * ��֤����ʱ���ֲ���Ψһ
     * @param columnValue
     * @param columnDisc
     * @param columnName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentColumn(String columnValue, String columnDisc,
            String columnName, ResponseDto responseDto) {
        // TODO Auto-generated method stub
        return this.checkEquimentColumn(columnValue, columnDisc, columnName,0,
                responseDto);
    }
    /**
     * ��֤����ʱ���ֲ���Ψһ
     * @param columnValue
     * @param columnDisc
     * @param columnName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentColumn(String columnValue, String columnDisc,
            String columnName, int i, ResponseDto responseDto) {
        // TODO Auto-generated method stub
        EquimentInfo equimentInfo = new EquimentInfo();
        if ("EQUIMENT_NAME".equals(columnName)) {
            equimentInfo.setEquimentName(columnValue);
        }
        if ("MODUBUS_ADDRESS".equals(columnName)) {
            equimentInfo.setModubusAdress(columnValue);
        }
        List<EquimentInfo> equimentForMaps = equimentDao
                .findEquimentInfo(equimentInfo);
        if (equimentForMaps != null && equimentForMaps.size() > 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + columnDisc
                    + "��" + columnValue + "���ѱ�ʹ��");
            return false;
        }
        return true;
    }

    /**
     * ��֤����ʱ���ֲ���Ψһ
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkEquimentColumn(String columnValue, String columnDisc,
            String columnName,String equimentName, ResponseDto responseDto) {
        return this.checkEquimentColumn(columnValue, columnDisc, columnName, equimentName,0,
                responseDto);
    }

    /**
     * ��֤����ʱ���ֲ���Ψһ
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkEquimentColumn(String columnValue, String columnDisc,
            String columnName,String equimentName , int i, ResponseDto responseDto) {
        // TODO Auto-generated method stub
        EquimentInfo equimentInfo = new EquimentInfo();
        if ("EQUIMENT_NAME".equals(columnName)) {
            equimentInfo.setEquimentName(columnValue);
        }
        if ("MODUBUS_ADDRESS".equals(columnName)) {
            equimentInfo.setModubusAdress(columnValue);
        }
        List<EquimentInfo> equimentForMaps = equimentDao
                .findEquimentInfo(equimentInfo);
        if (equimentForMaps != null && equimentForMaps.size() > 0) {
            if (equimentName.equals(equimentForMaps.get(0).getEquimentName())) {
                return true;
            }
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + columnDisc
                    + "��" + columnValue + "���ѱ�ʹ��");
            return false;
        }
        return true;
    }

    /**
     * ��鴴�������豸�Ĳ���
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentName(EquimentInfo equimentInfo,
            ResponseDto responseDto) {
        if (!checkEquimentInfo(equimentInfo, responseDto)) {
            return false;
        }

        if (!checkNameAdress(equimentInfo, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * ��֤�豸���ƺ�modubus��ַ��Ϊ��
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkNameAdress(EquimentInfo equimentInfo,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        return this.checkNameAdress(equimentInfo, 0, responseDto);
    }

    /**
     * ��֤�豸���ƺ�modubus��ַ��Ϊ��
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkNameAdress(EquimentInfo equimentInfo, int i,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        if (StringUtil.isEmpty(equimentInfo.getEquimentName())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�豸��Ϊ��");
            return false;
        }
        if (StringUtil.isEmpty(equimentInfo.getModubusAdress())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�豸��ַΪ��");
            return false;
        }
        return true;
    }

    /**
     * ��֤���������豸��Ϣ��Ϊ��
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentInfo(EquimentInfo equimentInfo,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        return this.checkEquimentInfo(equimentInfo, 0, responseDto);
    }

    /**
     * ��֤���������豸��Ϣ��Ϊ��
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentInfo(EquimentInfo equimentInfo, int i,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        if (equimentInfo == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�豸��ϢΪ��");
            return false;
        }
        return true;
    }

    /**
     * ���beanû�е�����
     * 
     * @param equimentInfo
     */
    private void addOther(EquimentInfo equimentInfo) {
        // TODO Auto-generated method stub
        equimentInfo.setDeleteFlag("0");

    }

    /**
     * ��֤����ʱ��id����
     * 
     * @param equimentName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentIdByDB(String equimentName,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        if (equimentName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ���豸");
            return false;
        }
        EquimentInfo equimentInfo = new EquimentInfo();
        equimentInfo.setEquimentName(equimentName);
        List<EquimentInfo> list = equimentDao.findEquimentInfo(equimentInfo);
        if (list == null || list.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ����豸������");
            return false;
        }
        responseDto.setResponseObject(list.get(0).getId());
        return true;
    }

    /**
     * �����豸��Ϣ�Ĳ������
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentParameterUpdate(EquimentInfo equimentInfo,
            ResponseDto responseDto) {
        if (!checkEquimentParameter(equimentInfo, responseDto)) {
            return false;
        }
        if (!checkEquimentId(equimentInfo.getEquimentName(), responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * ɾ���豸�Ĳ������
     * 
     * @param equimentName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentId(String equimentName, ResponseDto responseDto) {
        return this.checkEquimentId(equimentName,0,responseDto);
    }
    /**
     * ɾ���豸�Ĳ������
     * 
     * @param equimentName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentId(String equimentName, int i,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        if (equimentName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("��ѡ���豸");
            return false;
        }
        return true;
    }

    /**
     * ��鴴�������豸
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentParameter(EquimentInfo equimentInfo,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        if (!checkEquimentInfo(equimentInfo, responseDto)) {
            return false;
        }
        if (!checkEquimentNameAdress(equimentInfo, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * ��֤�豸���͵�ַ����Ϊ��
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentNameAdress(EquimentInfo equimentInfo,
            ResponseDto responseDto) {
        // TODO Auto-generated method stub
        return this.checkEquimentNameAdress(equimentInfo, 0, responseDto);
    }

    /**
     * ��֤�豸���͵�ַ����Ϊ��
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentNameAdress(EquimentInfo equimentInfo, int i,
            ResponseDto responseDto) {
        if (StringUtil.isEmptyString(equimentInfo.getEquimentName())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�豸��Ϊ��");
            return false;
        }
        if (StringUtil.isEmptyString(equimentInfo.getModubusAdress())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("��" + i + "��")) + "�豸��ַΪ��");
            return false;
        }
        return true;
    }

}
