/**
 */
package com.huayun.test3.scan.scan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lyf  
 */
@Configuration
@ComponentScan("com.huayun.test3.scan.service")
public class B {
	/**
	 * 
	 */
	public B() {
		// TODO Auto-generated constructor stub
		System.out.println("B");

	}

}
