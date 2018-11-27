/**
 */
package com.huayun.test3.scan.t1;

import org.springframework.beans.factory.annotation.Autowired;

import com.huayun.test3.scan.H;

/**
 * @author lyf  
 */
public class N {

	/**
	 * 
	 */
	public N() {
		// TODO Auto-generated constructor stub
		System.out.println("N");

	}
	
	public H h;

	public H getH() {
		return h;
	}
	@Autowired
	public void setH(H h) {
		this.h = h;
	}

}
