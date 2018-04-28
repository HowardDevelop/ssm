package org.ssm.dufy.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ssm.dufy.entity.EquimentInfo;
import org.ssm.dufy.entity.UserInfo;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({ "classpath:applicationContext.xml"})
public class IUserServiceTest {

	@Autowired
	public UserService userService;
	
	@Test
	public void getUserByIdTest() throws Exception{
	 UserInfo userInfo = new UserInfo();
	 String userName="Rex";
	 userInfo.setName("");
	 userInfo.setPhone("133223");
	 userInfo.setPassword("123");
	 System.out.println(userInfo);
	 String equimentName="plcCS1";
	 EquimentInfo equimentInfo = new EquimentInfo();
	 equimentInfo.setEquimentName("plcCS1");
	 equimentInfo.setModubusAdress("192.168.1.222");
	String result = userService.getUserEquiment(userName);
		System.out.println(result);
	}

}
