/**
 */
package com.huayun.test3.scan.scan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.huayun.test3.scan.E;
import com.huayun.test3.scan.I;

/**
 * @author lyf  
 */
@Import(E.class)
public class A {
	/**
	 * 
	 */
	public A() {
		// TODO Auto-generated constructor stub
		System.out.println("A");

	}
	@Bean
	public I i(){
		return new I();
	}

}
