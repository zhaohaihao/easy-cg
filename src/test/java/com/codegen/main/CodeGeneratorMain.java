package com.codegen.main;

import com.codegen.service.CodeGeneratorManager;

/**
 * 代码生成器启动项
 * Created by zhh on 2017/09/20.
 */
public class CodeGeneratorMain {
	
	private static final String TABLE = "gen_test_demo";
	
	private static final String MODEL_NAME = "ITest";
	
	private static final String[] TABLES = {
			"gen_test_demo", "gen_test_demo1"
	};
	
	/**
	 * 说明:
	 * 以表名 gen_test_demo 为例子, 主要是以下几种情况:
	 * 		1. gen_test_demo ==> Demo 可以传入多表
	 * 		genCodeWithSimpleName("gen_test_demo");
	 * 		
	 * 		2. gen_test_demo ==> GenTestDemo 可以传入多表
	 * 		genCodeWithDetailName("gen_test_demo");
	 * 
	 * 		3. gen_test_demo ==> IDemo 自定义名称
	 * 		genCodeWithCustomName("gen_test_demo", "IDemo");
	 */
	public static void main(String[] args) {
		CodeGeneratorManager cgm = new CodeGeneratorManager();
		
		cgm.genCodeWithSimpleName(TABLE);
		
//		cgm.genCodeWithDetailName(TABLES);
		
//		cgm.genCodeWithCustomName(TABLE, MODEL_NAME);
	}
}
