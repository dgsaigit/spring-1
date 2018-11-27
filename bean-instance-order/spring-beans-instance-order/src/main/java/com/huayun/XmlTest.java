/**
 */
package com.huayun;

import java.io.IOException;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.huayun.test3.Test;

/**
 * @author lyf
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Test.class)
public class XmlTest {

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@org.junit.Test
	public void testRun() {
		System.out.println(webApplicationContext.toString());
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-beans.xml");
		System.out.println(context.getDisplayName() + ": here");
		context.start();

	}

}
