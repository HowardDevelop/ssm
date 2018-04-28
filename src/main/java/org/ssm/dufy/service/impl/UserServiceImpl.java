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
 // 密码加密
    private Encrypt encrypt = new Encrypt();

    /**
     * 登陆验证
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
                        responseDto.setErrmsg("验证登陆成功！");
                        responseDto.setResponseObject(list.get(0));
                        return JsonUtils.bean2json(responseDto);
                    }else {
                        responseDto.setErrcode("1");
                        responseDto.setErrmsg("密码错误");
                        return JsonUtils.bean2json(responseDto);
                    }
                }
                responseDto.setErrcode("1");
                responseDto.setErrmsg("用户不存在");
            }
        } catch (Exception e) {
            // TODO: handle exception
            responseDto.setErrcode("1");
            responseDto.setErrmsg("验证登陆信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 查询用户信息
     * 
     * @param name
     * @return
     * @throws Exception
     */
    public String selectUserMsg(String userName) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // 数据整合性check
            if (!checkUserName(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setName(userName);
            List<UserInfo> list = userDao.findUserInfo(userInfo);
            if (null == list || list.size() == 0) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg(userName + "用户不存在");
                return JsonUtils.bean2json(responseDto);
            }
            userInfo = list.get(0);
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("查询成功");
            responseDto.setResponseObject(userInfo);// 返回用户信息
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("查询用户信息异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询用户信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 创建用户
     * 
     * @param UserInfo
     * @return
     */
    public String addUserMsg(UserInfo user) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // 数据整合性check
            if (!checkUserParameter(user, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 判断是否已被注册
            if (checkUserColumn(user.getName(), "用户名", "NAME", responseDto)
                    && checkUserColumn(user.getPhone(), "手机号", "PHONE",
                            responseDto)) {
            }
            addOther(user);// 添加javabean没有的其他字段
            user.setPassword(encrypt.encoder(user.getPassword()));
            userDao.addEntity(user);
            responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("添加成功");
            responseDto.setResponseObject(user);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("添加用户异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("添加用户信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 更新用户信息
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
            // 数据整合性check
            if (!checkUserParameterUpdate(userInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 指定用户数据存在check
            if (!checkUserIdByDB(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 判断是否已被注册
            if (checkUserColumn(userInfo.getName(), "用户名", "NAME", userName,
                    responseDto)
                    && checkUserColumn(userInfo.getPhone(), "手机号", "PHONE",
                            userName, responseDto)) {
                userInfo.setId(responseDto.getResponseObject().toString());
                userDao.editEntity(userInfo);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("更新成功");
                // responseDto.setResponseObject(userInfo);
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("更新用户异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("更新用户信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 删除用户
     * 
     * @param name
     * @return
     */
    public String deleteUserMsg(String name) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        UserInfo userInfo = new UserInfo();
        try {
            // 数据整合性check
            if (!checkUserName(name, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 指定用户数据存在check
            if (!checkUserNameByDB(name, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            userInfo.setId(responseDto.getResponseObject().toString());
            userInfo.setDeleteFlag("1");
            userDao.editEntity(userInfo);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("删除成功");
            // responseDto.setResponseObject(userInfo);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("删除用户异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("删除用户异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 获取设备信息
     * 
     * @param equimentName
     */
    public String selectEquimentMsg(String equimentName) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        EquimentInfo equimentInfo = new EquimentInfo();
        try {
            // 数据整合性check
            if (!checkEquimentName(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            equimentInfo.setEquimentName(equimentName);
            List<EquimentInfo> list = equimentDao
                    .findEquimentInfo(equimentInfo);
            if (null == list || list.size() == 0) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg(equimentName + "设备不存在");
                return JsonUtils.bean2json(responseDto);
            }
            equimentInfo = list.get(0);
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("查询成功");
            responseDto.setResponseObject(equimentInfo);// 返回用户信息
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("查询设备信息异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询设备信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 创建设备
     * 
     * @param equimentInfo
     */
    public String addEquimentMsg(EquimentInfo equimentInfo) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // 数据整合性check
            if (!checkEquimentName(equimentInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 判断是否已被注册
            if (checkEquimentColumn(equimentInfo.getEquimentName(), "设备名称",
                    "EQUIMENT_NAME", responseDto)
                    && checkEquimentColumn(equimentInfo.getModubusAdress(),
                            "设备modubus地址", "MODUBUS_ADDRESS", responseDto)) {

                addOther(equimentInfo);// 添加javabean没有的其他字段
                equimentDao.addEntity(equimentInfo);
                responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("添加成功");
                // responseDto.setResponseObject(equimentInfo);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("添加设备异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("添加设备异常");
        }
        return JsonUtils.bean2json(responseDto);
    }


    /**
     * 更新设备信息
     * 
     * @param equimentInfo
     * @param equimentName
     */
    public String updateEquimentMsg(String equimentName,
            EquimentInfo equimentInfo) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // 数据整合性check
            if (!checkEquimentParameterUpdate(equimentInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 指定用户数据存在check
            if (!checkEquimentIdByDB(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 判断是否已被注册
            if (checkEquimentColumn(equimentInfo.getEquimentName(), "设备名称",
                    "EQUIMENT_NAME",equimentName, responseDto)
                    && checkEquimentColumn(equimentInfo.getModubusAdress(),
                            "设备地址", "MODUBUS_ADDRESS", equimentName,responseDto)) {
                equimentInfo.setId(responseDto.getResponseObject().toString());
                equimentDao.editEntity(equimentInfo);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("更新成功");
                // responseDto.setResponseObject(equimentInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新设备异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("更新设备异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 删除设备信息
     */
    public String deleteEquimentMsg(String equimentName) throws Exception {
        // TODO Auto-generated method stub
        ResponseDto responseDto = new ResponseDto();
        try {
            // 数据整合性check
            if (!checkEquimentName(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 指定用户数据存在check
            if (!checkEquimentNameByDB(equimentName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            EquimentInfo equimentInfo = new EquimentInfo();
            equimentInfo.setId(responseDto.getResponseObject().toString());
            equimentInfo.setDeleteFlag("1");
            equimentDao.editEntity(equimentInfo);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("删除成功");
            // responseDto.setResponseObject(equimentInfo);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("删除设备异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("删除设备异常");
        }
        return JsonUtils.bean2json(responseDto);
    }
    /**
     * 获取用户设备信息
     * 
     * @param accessToken
     * @param userName
     * @return
     */
    public String getUserEquiment(String userName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // 数据整合性check
            if (!checkUserName(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            String[] roleName = getEquimentIdByUserName(userName, responseDto);
            if (roleName == null) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("该用户还未拥有可查询设备或该用户不存在");
            } else {
                responseDto.setErrcode("0");
                responseDto.setErrmsg("查询成功");
                responseDto.setResponseObject(roleName);// 返回用户信息
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户设备信息异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询用户设备信息异常");
        }

        return JsonUtils.bean2json(responseDto);
    }
    /**
     * 通过用户名获取设备名
     * 
     * @param userName
     * @param responseDto
     * @return
     */
    private String[] getEquimentIdByUserName(String userName, ResponseDto responseDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userName);
        List<UserInfo> users = userDao.findUserInfo(userInfo);// 用户信息查询
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
     * 验证设备名称是否存在
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
            responseDto.setErrmsg("设备(" + equimentName + ")不存在");
            return false;
        }
        String Id = equiInfos.get(0).getId();
        responseDto.setResponseObject(Id);
        return true;
    }

    /**
     * 添加bean没有的其他字段
     * 
     * @param apiUserFormMap
     */
    private void addOther(UserInfo user) {
        user.setDeleteFlag("0");
    }

    /**
     * 验证更新时部分参数唯一
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
     * 验证更新时部分参数唯一
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
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + columnDisc
                    + "（" + columnValue + "）已被使用");
            return false;
        }
        return true;
    }
   
    /**
     * 检查创建单个用户的参数
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
     * 验证用户名和密码不能为空
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkNamePassword(UserInfo userInfo, ResponseDto responseDto) {
        return this.checkNamePassword(userInfo, 0, responseDto);
    }

    /**
     * 验证用户名和密码不能为空
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkNamePassword(UserInfo userInfo, int i,
            ResponseDto responseDto) {
        if (userInfo.getName().isEmpty()) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "用户名为空");
            return false;
        }
        if (userInfo.getPassword().isEmpty()) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "密码为空");
            return false;
        }if(userInfo.getPhone().isEmpty()) {
        	responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "手机号为空");
            return false;
        }
        return true;
    }

    /**
     * 验证传进来的用户信息不为空
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
     * 验证传进来的用户信息不为空
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserInfo(UserInfo userInfo, int i,
            ResponseDto responseDto) {
        if (userInfo == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "用户信息为空");
            return false;
        }

        return true;
    }

    /**
     * 验证更新时部分参数唯一
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
     * 验证更新时部分参数唯一
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
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + columnDisc
                    + "（" + columnValue + "）已被使用");
            return false;
        }
        return true;
    }

    /**
     * 验证更新时userId存在
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserIdByDB(String userName, ResponseDto responseDto) {
        if (userName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择用户");
            return false;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userName);
        List<UserInfo> lists = userDao.findUserInfo(userInfo);
        if (lists == null || lists.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("你选择的用户不存在");
            return false;
        }
        responseDto.setResponseObject(lists.get(0).getId());
        return true;
    }

    /**
     * 更新用户信息的参数检查
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
     * 删除用户的参数检查
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
     * 删除用户的参数检查
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserId(String userName, int i, ResponseDto responseDto) {
        if (userName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择" + (i == 0 ? "" : "第" + i + "个") + "用户");
            return false;
        }
        return true;
    }

    /**
     * 删除用户的参数检查
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
     * 删除用户的参数检查
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
            responseDto.setErrmsg("请选择" + (i == 0 ? "" : "第" + i + "个") + "用户");
            return false;
        }
        return true;
    }

    /**
     * 检查用户是否存在
     * 
     * @param userName
     * @param i
     * @param responseDto
     * @return
     */
    private boolean checkUserNameByDB(String userName, ResponseDto responseDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userName);
        List<UserInfo> userInfos = userDao.findUserInfo(userInfo);// 用户信息查询
        if (userInfos == null || userInfos.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("用户(" + userName + ")不存在");
            return false;
        }
        String userId = userInfos.get(0).getId();
        responseDto.setResponseObject(userId);
        return true;
    }

    /**
     * 删除设备的参数检查
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
     * 删除设备的参数检查
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
            responseDto.setErrmsg("请选择" + (i == 0 ? "" : "第" + i + "个") + "设备");
            return false;
        }
        return true;
    }
    /**
     * 验证更新时部分参数唯一
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
     * 验证更新时部分参数唯一
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
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + columnDisc
                    + "（" + columnValue + "）已被使用");
            return false;
        }
        return true;
    }

    /**
     * 验证更新时部分参数唯一
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
     * 验证更新时部分参数唯一
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
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + columnDisc
                    + "（" + columnValue + "）已被使用");
            return false;
        }
        return true;
    }

    /**
     * 检查创建单个设备的参数
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
     * 验证设备名称和modubus地址不为空
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
     * 验证设备名称和modubus地址不为空
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
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "设备名为空");
            return false;
        }
        if (StringUtil.isEmpty(equimentInfo.getModubusAdress())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "设备地址为空");
            return false;
        }
        return true;
    }

    /**
     * 验证传进来的设备信息不为空
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
     * 验证传进来的设备信息不为空
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
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "设备信息为空");
            return false;
        }
        return true;
    }

    /**
     * 添加bean没有的属性
     * 
     * @param equimentInfo
     */
    private void addOther(EquimentInfo equimentInfo) {
        // TODO Auto-generated method stub
        equimentInfo.setDeleteFlag("0");

    }

    /**
     * 验证更新时，id存在
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
            responseDto.setErrmsg("请选择设备");
            return false;
        }
        EquimentInfo equimentInfo = new EquimentInfo();
        equimentInfo.setEquimentName(equimentName);
        List<EquimentInfo> list = equimentDao.findEquimentInfo(equimentInfo);
        if (list == null || list.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("你选择的设备不存在");
            return false;
        }
        responseDto.setResponseObject(list.get(0).getId());
        return true;
    }

    /**
     * 更新设备信息的参数检查
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
     * 删除设备的参数检查
     * 
     * @param equimentName
     * @param responseDto
     * @return
     */
    private boolean checkEquimentId(String equimentName, ResponseDto responseDto) {
        return this.checkEquimentId(equimentName,0,responseDto);
    }
    /**
     * 删除设备的参数检查
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
            responseDto.setErrmsg("请选择设备");
            return false;
        }
        return true;
    }

    /**
     * 检查创建单个设备
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
     * 验证设备名和地址不能为空
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
     * 验证设备名和地址不能为空
     * 
     * @param equimentInfo
     * @param responseDto
     * @return
     */
    private boolean checkEquimentNameAdress(EquimentInfo equimentInfo, int i,
            ResponseDto responseDto) {
        if (StringUtil.isEmptyString(equimentInfo.getEquimentName())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "设备名为空");
            return false;
        }
        if (StringUtil.isEmptyString(equimentInfo.getModubusAdress())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "设备地址为空");
            return false;
        }
        return true;
    }

}
