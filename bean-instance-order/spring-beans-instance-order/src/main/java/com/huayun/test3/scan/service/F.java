/**
 */
package com.huayun.test3.scan.service;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import com.huayun.test3.scan.t1.Q;

/**
 * @author lyf  
 */
@Component
@Import(Q.class)
@ImportResource(locations="classpath:import-resource.xml")
public class F {
	/**
	 * 
	 */
	public F() {
		// TODO Auto-generated constructor stub
		System.out.println("F");

	}


}
