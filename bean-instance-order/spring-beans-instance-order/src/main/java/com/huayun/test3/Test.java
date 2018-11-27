/**
 */
package com.huayun.test3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.huayun.test3.scan.C;
import com.huayun.test3.scan.H;
import com.huayun.test3.scan.scan.A;

/**
 * @author lyf  
 */
@ComponentScan("com.huayun.test3.scan.scan")
@Configuration
@Import(value={A.class,C.class})
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
		System.out.println("Test");
	}
	
	@Bean
	public H h(){
		return new H();
	}


}
