package com.codegen.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;

import com.codegen.service.impl.ControllerGenerator;
import com.codegen.service.impl.ModelAndMapperGenerator;
import com.codegen.service.impl.ServiceGenerator;
import com.codegen.util.StringUtils;
import com.google.common.base.CaseFormat;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * 代码生成器基础项 (常量信息 & 通用方法)
 * Created by zhh on 2017/09/20.
 */
public class CodeGeneratorManager {
	
	// JDBC 相关配置信息
	protected static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
	protected static final String JDBC_USERNAME = "root";
	protected static final String JDBC_PASSWORD = "root";
	protected static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	// 项目在硬盘上的基础路径
	protected static final String PROJECT_PATH = System.getProperty("user.dir");
	// 模板存放位置
	protected static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources/generator/template";
	// java文件路径
	protected static final String JAVA_PATH = "/src/main/java";
	// 资源文件路径
	protected static final String RESOURCES_PATH = "/src/main/resources";
	
	// 项目基础包
	protected static final String BASE_PACKAGE = "com.bigsea.sns";
	// 项目 Model 所在包
	protected static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";
	// 项目 Mapper 所在包
	protected static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao.mapper";
	// 项目 Service 所在包
	protected static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
	// 项目 Service 实现类所在包
	protected static final String SERVICE_IMPL_PACKAGE = BASE_PACKAGE + ".service.impl";
	// 项目 Controller 所在包
	protected static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web.controller";
	
	// 生成的 Service 存放路径
	protected static final String PACKAGE_PATH_SERVICE = packageConvertPath(SERVICE_PACKAGE);
	// 生成的 Service 实现存放路径
	protected static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(SERVICE_IMPL_PACKAGE);
	// 生成的 Controller 存放路径
	protected static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);

	// 模板注释中 @author
	protected static final String AUTHOR = "zhh";
	// 模板注释中 @date
	protected static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	
	private static Configuration configuration = null;
	
	/**
	 * 获取 Freemarker 模板环境配置
	 * @return
	 */
	public Configuration getFreemarkerConfiguration() {
		if (configuration == null) {
			configuration = initFreemarkerConfiguration();
			return initFreemarkerConfiguration();
		}
		return configuration;
	}
	
	/**
	 * Mybatis 代码自动生成基本配置
	 * @return
	 */
	public Context initMybatisGeneratorContext(String sign) {
		Context context = new Context(ModelType.FLAT);
		context.setId("Potato");
		context.setTargetRuntime("MyBatis3Simple");
		context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");
        
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(JDBC_URL);
        jdbcConnectionConfiguration.setUserId(JDBC_USERNAME);
        jdbcConnectionConfiguration.setPassword(JDBC_PASSWORD);
        jdbcConnectionConfiguration.setDriverClass(JDBC_DIVER_CLASS_NAME);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("mapper." + sign);
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        
		return context;
	}
	
	/**
	 * 生成简单名称代码
	 * eg: 
	 * 	genCode("gen_test_demo");  gen_test_demo ==> Demo
	 * @param tableNames 表名, 可以多表
	 */
	public void genCodeWithSimpleName(String ...tableNames) {
		genCodeByTableName(true, tableNames);
	}
	
	/**
	 * 生成具体名称代码
	 * eg: 
	 * 	genCode("gen_test_demo");  gen_test_demo ==> GenTestDemo
	 * @param tableNames 表名, 可以多表
	 */
	public void genCodeWithDetailName(String ...tableNames) {
		genCodeByTableName(false, tableNames);
	}
	
	/**
	 * 生成自定义名称代码
	 * eg: 
	 * 	genCode("gen_test_demo", "IDemo");  gen_test_demo ==> IDemo
	 * @param tableName 表名, 只能单表
	 */
	public void genCodeWithCustomName(String tableName, String customModelName) {
		genCodeByTableName(tableName, customModelName, false);
	}
	
	/**
	 * 下划线转成驼峰, 首字符为小写
	 * eg: gen_test_demo ==> genTestDemo
	 * @param tableName 表名, eg: gen_test_demo
	 * @return
	 */
	protected String tableNameConvertLowerCamel(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
	}
	
	/**
	 * 下划线转成驼峰, 首字符为大写
	 * eg: gen_test_demo ==> GenTestDemo
	 * @param tableName 表名, eg: gen_test_demo
	 * @return
	 */
	protected String tableNameConvertUpperCamel(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
	}
	
	/**
	 * 表名转成映射路径
	 * eg: gen_test_demo ==> /gen/test/demo
	 * @param tableName 表名
	 * @return
	 */
	protected String tableNameConvertMappingPath(String tableName) {
		tableName = tableName.toLowerCase();
		return File.separator + (tableName.contains("_") ? tableName.replaceAll("_", File.separator) : tableName);
	}
	
	/**
	 * ModelName转成映射路径
	 * eg: Demo ==> /demo
	 * @param modelName
	 * @return
	 */
	protected String modelNameConvertMappingPath(String modelName) {
		String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
		return tableNameConvertMappingPath(tableName);
	}
	
	/**
	 * 获取表的区分字段
	 * @param tableName 表名, eg: gen_test_demo
	 * @return 区分字段 eg: test
	 */
	protected String getSign(String tableName) {
		return getTableNameSplit(tableName)[1];
	}
	
	/**
	 * 获取默认 modelName
	 * @param tableName 表名
	 * @return
	 */
	protected String getDefModelName(String tableName) {
		String[] strs = getTableNameSplit(tableName);
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < strs.length; i++) {
			sb.append(StringUtils.toUpperCaseFirstOne(strs[i].toLowerCase()));
		}
		return sb.toString();
	}
	
	/**
	 * 获取表名切割后的数组
	 * @param tableName 表名
	 * @return
	 */
	private String[] getTableNameSplit(String tableName) {
		String[] strs = tableName.split("_");
		if (!tableName.contains("_") || strs.length < 3) {
			throw new RuntimeException("表名格式不正确, 请按规定格式! 例如: gen_test_demo");
		}
		return strs;
	}
	
	/**
	 * 通过数据库表名, 生成代码
	 * 如表名为 gen_test_demo
	 * 将生成  Demo & DemoMapper & DemoService & DemoServiceImpl & DemoController
	 * @param flag 标志
	 * @param tableNames 表名数组
	 */
	private void genCodeByTableName(boolean flag, String ...tableNames) {
		for (String tableName : tableNames) {
			genCodeByTableName(tableName, null, flag);
		}
	}
	
	/**
	 * 通过数据库表名, 和自定义 modelName 生成代码
	 * 如表名为 gen_test_demo, 自定义 modelName 为 IDemo
	 * 将生成  IDemo & IDemoMapper & IDemoService & IDemoServiceImpl & IDemoController
	 * @param tableName 表名
	 * @param modelName 实体类名
	 * @param flag 标志
	 */
	private void genCodeByTableName(String tableName, String modelName, boolean flag) {
		String sign = getSign(tableName);
		if (flag) {
			modelName = getDefModelName(tableName);
		}
		new ModelAndMapperGenerator().genCode(tableName, modelName, sign);
		new ServiceGenerator().genCode(tableName, modelName, sign);
		new ControllerGenerator().genCode(tableName, modelName, sign);
	}
	
	/**
	 * Freemarker 模板环境配置
	 * @return
	 * @throws IOException
	 */
	private Configuration initFreemarkerConfiguration() {
		Configuration cfg = null;
		try {
			cfg = new Configuration(Configuration.VERSION_2_3_23);
			cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		} catch (IOException e) {
			throw new RuntimeException("Freemarker 模板环境初始化异常!", e);
		}
		return cfg;
	}
	
	/**
	 * 包转成路径
	 * eg: com.bigsea.sns ==> com/bigsea/sns
	 * @param packageName
	 * @return
	 */
	protected static String packageConvertPath(String packageName) {
		return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
	}
}
