package com.codegen.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.codegen.service.CodeGenerator;
import com.codegen.service.CodeGeneratorManager;
import com.codegen.util.StringUtils;

/**
 * Model & Mapper 代码生成器
 * Created by zhh on 2017/09/20.
 */
public class ModelAndMapperGenerator extends CodeGeneratorManager implements CodeGenerator {

	@Override
	public void genCode(String tableName, String modelName, String sign) {
		Context initConfig = initConfig(tableName, modelName, sign);
		List<String> warnings = null;
		MyBatisGenerator generator = null;
		try {
			Configuration cfg = new Configuration();
			cfg.addContext(initConfig);
			cfg.validate();
			
			DefaultShellCallback callback = new DefaultShellCallback(true);
			warnings = new ArrayList<String>();
			generator = new MyBatisGenerator(cfg, callback, warnings);
			generator.generate(null);
		} catch (Exception e) {
			throw new RuntimeException("Model 和  Mapper 生成失败!", e);
		}
		
		if (generator == null || generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
			throw new RuntimeException("Model 和  Mapper 生成失败, warnings: " + warnings);
		}
		
		if (StringUtils.isNullOrEmpty(modelName)) {
			modelName = tableNameConvertUpperCamel(tableName);
		}
		
		logger.info(modelName, "{}.java 生成成功!");
		logger.info(modelName, "{}Mapper.java 生成成功!");
		logger.info(modelName, "{}Mapper.xml 生成成功!");
	}
	
	/**
	 * 完善初始化环境
	 * @param tableName 表名
	 * @param modelName 自定义实体类名, 为null则默认将表名下划线转成大驼峰形式
	 * @param sign 区分字段, 规定如表 gen_test_demo, 则 test 即为区分字段
	 */
	private Context initConfig(String tableName, String modelName, String sign) {
		Context context = null;
		try {
			context = initMybatisGeneratorContext(sign);
			JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
	        javaModelGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
	        javaModelGeneratorConfiguration.setTargetPackage(MODEL_PACKAGE + "." + sign);
	        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
	        
	        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
	        javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
	        javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE + "." + sign);
	        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
	        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
	        
	        TableConfiguration tableConfiguration = new TableConfiguration(context);
	        tableConfiguration.setTableName(tableName);
	        tableConfiguration.setDomainObjectName(modelName);
	        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
	        context.addTableConfiguration(tableConfiguration);
		} catch (Exception e) {
			throw new RuntimeException("ModelAndMapperGenerator 初始化环境异常!", e);
		}
		return context;
	}
}
