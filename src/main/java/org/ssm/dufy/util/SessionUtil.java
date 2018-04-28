package org.ssm.dufy.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

public class SessionUtil {
    // �����쳣��־��¼����
    private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);

    /**
     * ��ȡrequest,
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request;
    }

    /**
     * ��ȡsession,
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * ��ȡ��¼�˺ŵ�ID
     * 
     * @return userid
     */
    public static String getUserIdSession() {
        Object obj = getSession().getAttribute(BaseConstants.SESSION_USER_ID_KEY);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    /**
     * ��ȡ��¼�˺ŵĵĶ���
     * 
     * @return Object ������Object..��Ҫת��Ϊ(Account)Object
     */
    public static Object getUserSession() {
        return (Object) getSession().getAttribute(BaseConstants.SESSION_USER_KEY);
    }
    
    /**
     * ��ȡ��¼�˺ŵĵ�ƾ֤
     * 
     * @return Object ������Object..��Ҫת��Ϊ(Account)Object
     */
    public static String getAccessTokenSession() {
        return (String) getSession().getAttribute(BaseConstants.SESSION_USER_ACCESS_TOKEN_KEY);
    }

    /**
     * �趨��¼�˺ŵ�ID��session��
     * 
     * @param userid 
     * @return
     */
    public static void setUserIdSession(String userName) {
        getSession().setAttribute(BaseConstants.SESSION_USER_ID_KEY, userName);
    }

    /**
     * �趨��¼�˺ŵĶ���session��
     * 
     * @param Object 
     */
    public static void setUserSession(Object object) {
        getSession().setAttribute(BaseConstants.SESSION_USER_KEY, object);
    }
    
    /**
     * �趨��¼�˺ŵ�Ȩ�޵�session��
     * 
     * @param Object 
     */
    public static void setAccessTokenSession(String accessToken) {
        getSession().setAttribute(BaseConstants.SESSION_USER_ACCESS_TOKEN_KEY, accessToken);
    }

    /**
     * ���session�еĵ�¼�˺ŵ�ID��Ϣ
     */
    public static void removeUserIdSession() {
        getSession().removeAttribute(BaseConstants.SESSION_USER_ID_KEY);
    }

    /**
     * ���session�еĵ�¼�˺ŵĶ�����Ϣ
     */
    public static void removeUserSession() {
        getSession().removeAttribute(BaseConstants.SESSION_USER_KEY);
    }
    
    /**
     * ���session�еĵ�¼�˺ŵ�ƾ֤��Ϣ
     */
    public static void removeAccessTokenSession() {
        getSession().removeAttribute(BaseConstants.SESSION_USER_ACCESS_TOKEN_KEY);
    }

    /**
     * ��ȡҳ�洫�ݵ�ĳһ������ֵ,
     */
    public static String getPara(String key) {
        return getRequest().getParameter(key);
    }

    /**
     * ��ȡҳ�洫�ݵ�ĳһ������ֵ,
     * 
     * @return Class<T>
     * @throws Exception
     */
    public static String[] getParaValues(String key) {
        String[] params = getRequest().getParameterValues(key);
        if (params != null) return params;
        return new String[]{getRequest().getParameter(key.substring(0, key.length()-2))};
    }

    /**
     * ��ȡRequest����ֵ,
     */
    public static Object getRequestAttr(String key) {
        return getRequest().getAttribute(key);
    }

    /**
     * �趨Request����ֵ,
     */
    public static void setRequestAttr(String key, Object obj) {
        getRequest().setAttribute(key, obj);
    }

    /**
     * ��ȡSession����ֵ,
     */
    public static Object getSessionAttr(String key) {
        return getSession().getAttribute(key);
    }

    /**
     * �趨Session����ֵ,
     */
    public static void setSessionAttr(String key, Object obj) {
        getSession().setAttribute(key, obj);
    }

    /**
     * ���Session����ֵ,
     */
    public static void removeSessionAttr(String key) {
        getSession().removeAttribute(key);
    }

    /**
     * ��ȡ�ͻ���IP��ַ,
     */
    public static String getIpAddress() throws IOException {
        return getIpAddress(getRequest());
    }

    /**
     * ��ȡ��������IP��ַ,���ͨ�������������͸������ǽ��ȡ��ʵIP��ַ;
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // ��ȡ��������IP��ַ,���ͨ�������������͸������ǽ��ȡ��ʵIP��ַ

        String ip = request.getHeader("X-Forwarded-For");
        if (logger.isInfoEnabled()) {
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * ��ȡ���ݵ����в���, ����ʵ������������������ֵ ͨ�����ͻش�����.
     * 
     * @return Class<T>
     * @throws Exception
     */
    public static JSONObject getJsonParam() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Enumeration<String> en = request.getParameterNames();
        JSONObject object = new JSONObject();
        try {
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                if (nms.endsWith("[]")) {
                    String[] as = request.getParameterValues(nms);
                    object.put(nms, as);
                } else {
                    String as = request.getParameter(nms);
                    // ��������Ϊ��ʱ ��ʧ
                    object.put(nms, as);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
