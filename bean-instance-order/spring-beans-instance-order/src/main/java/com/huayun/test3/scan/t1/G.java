/**
 */
package com.huayun.test3.scan.t1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * @author lyf  
 */
@Component
@Import(K.class)
public class G {
	/**
	 * 
	 */
	public G() {
		// TODO Auto-generated constructor stub
		System.out.println("G");

	}
	@Bean
	public L l(){
		return new L();
	}

}
