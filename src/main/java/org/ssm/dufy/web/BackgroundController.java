package org.ssm.dufy.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.ssm.dufy.dao.SysUserDao;
import org.ssm.dufy.dao.UserDao;
import org.ssm.dufy.entity.SysUserInfo;
import org.ssm.dufy.util.AttrConstants;
import org.ssm.dufy.util.ForwardConstants;
import org.ssm.dufy.util.SessionUtil;
import org.ssm.dufy.util.StringUtil;


/**
 *  ���й����̨��ܽ������
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/")
public class BackgroundController  {

    @Autowired
    private SysUserDao sysUserDao;

    /**
     * ��ҳ
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String toIndex() {
        return ForwardConstants.REDIRECT + ForwardConstants.LOGIN;
    }

    /**
     * 
     * �����½����
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
    public String login(HttpServletRequest request) {
        request.removeAttribute("error");
        return ForwardConstants.LOGIN;
    }

    /**
     * �û���¼��֤
     * 
     * @param username
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
    public String login(String userName, String password, HttpServletRequest request) {
        try {
            if (!request.getMethod().equals("POST")) {
                request.setAttribute(AttrConstants.ERROR, "֧��POST�����ύ��");
            }
            if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
                request.setAttribute(AttrConstants.ERROR, "�û��������벻��Ϊ�գ�");
                return ForwardConstants.LOGIN;
            }
            SysUserInfo sysUserInfo = new SysUserInfo();
            sysUserInfo.setUserName(userName);
            List<SysUserInfo> list = sysUserDao.findUserInfo(sysUserInfo);
            if (null != list && list.size() >0) {
            	 sysUserInfo = list.get(0);
            	if (password.equals(sysUserInfo.getPassword())) {
            		SessionUtil.setUserSession(sysUserInfo);
                    SessionUtil.setUserIdSession(sysUserInfo.getUserId().toString());
				}else {
                    request.setAttribute(AttrConstants.ERROR, "�û��������벻��ȷ��");
                    return ForwardConstants.LOGIN;
                }
			}
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(AttrConstants.ERROR, "��¼�쳣������ϵ����Ա��");
            return ForwardConstants.LOGIN;
        }
        return ForwardConstants.REDIRECT + ForwardConstants.INDEX;
    }
    /**
     * ������ҳ��
     * 
     * @mod
     * @throws Exception
     */
    @RequestMapping("index")
    public String index(Model model) throws Exception {
    	SysUserInfo sysUserInfo = (SysUserInfo) SessionUtil.getUserSession();
		return null;
    	
    }
}
