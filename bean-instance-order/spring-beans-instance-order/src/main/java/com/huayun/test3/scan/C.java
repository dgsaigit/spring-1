/**
 */
package com.huayun.test3.scan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.huayun.test3.scan.t1.M;
import com.huayun.test3.scan.t1.N;

/**
 * @author lyf  
 */
//@Component
@ComponentScan("com.huayun.test3.scan.t1")
@Import(N.class)
public class C {
	/**
	 * 
	 */
	public C() {
		// TODO Auto-generated constructor stub
		System.out.println("C");

	}
	@Bean
	public M m(){
		return new M();
	}
	

}

