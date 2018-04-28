package org.ssm.dufy.util;

import java.util.Locale;

/**
 * �����ӿ�
 * @author xieen
 */
public interface BaseConstants {
    //���Ի�������
    public static final Locale DEFAULT_LOCALE = Locale.CHINA;
    //Ĭ�Ϸ�ҳ����
    public static final int PAGE_SIZE_DEFAULT = 20;
    // ��¼�ʺŵ�SessionKey
    public static final String SESSION_USER_KEY = "userSession";
    //��¼�ʺ�ID��SessionKey
    public static final String SESSION_USER_ID_KEY = "userIdSession";
    //��½�˺�Ȩ�޵�SessionKey
    public static final String SESSION_USER_ACCESS_TOKEN_KEY = "accessToken";
    //APP��¼�ʺ�SessionKey
    public static final String APP_SESSION_USER_KEY = "appSessionUserKey";
    
    //��¼״̬
    public static final int STATUS_ISVALID_YES = 1;
    public static final int STATUS_ISVALID_NO  = 0;
}
