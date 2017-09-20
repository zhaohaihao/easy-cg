package com.codegen.service;
/**
 * 主要逻辑接口
 * Created by zhh on 2017/09/20.
 */
public interface CodeGenerator {
	
	/**
	 * 代码生成主要逻辑
	 * @param tableName 表名
	 * @param modelName 自定义实体类名, 为null则默认将表名下划线转成大驼峰形式
	 * @param sign 区分字段, 规定如表 gen_test_demo, 则 test 即为区分字段
	 */
	void genCode(String tableName, String modelName, String sign);
}
