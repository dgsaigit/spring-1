/**
 */
package com.huayun.test3.scan.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.huayun.test3.scan.t1.O;

/**
 * @author lyf  
 */
@Component
public class D {
	/**
	 * 
	 */
	public D() {
		// TODO Auto-generated constructor stub
		System.out.println("D");
	}
	@Bean
	public O o(){
		return new O();
	}

}
