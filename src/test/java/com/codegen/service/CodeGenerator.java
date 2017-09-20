package com.codegen.service;
/**
 * 主要逻辑接口
 * Created by zhh on 2017/09/20.
 */
public interface CodeGenerator {
	
	/**
	 * 代码生成主要逻辑
	 * @param tableName 表名
	 * @param modelName 实体类名
	 * @param sign 区分字段
	 */
	void genCode(String tableName, String modelName, String sign);
}
