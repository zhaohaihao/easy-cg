package com.codegen.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class CodeGeneratorManager extends CodeGeneratorConfig {
	
	protected static final Logger logger = LoggerFactory.getLogger(CodeGeneratorManager.class);
	
	private static Configuration configuration = null;
	
	static {
		// 初始化配置信息
		init();
	}
	
	/**
	 * 获取 Freemarker 模板环境配置
	 * @return
	 */
	public Configuration getFreemarkerConfiguration() {
		if (configuration == null) {
			configuration = initFreemarkerConfiguration();
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
        jdbcConnectionConfiguration.setDriverClass(JDBC_DRIVER_CLASS_NAME);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("mapper." + sign);
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        
        // 增加 mapper 插件
        addMapperPlugin(context);
        
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
	 * 增加 Mapper 插件
	 * @param context
	 */
	private void addMapperPlugin(Context context) {
		PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.addProperty("mappers", MAPPER_INTERFACE_REFERENCE);
        context.addPluginConfiguration(pluginConfiguration);
	}
	
	/**
	 * 包转成路径
	 * eg: com.bigsea.sns ==> com/bigsea/sns
	 * @param packageName
	 * @return
	 */
	private static String packageConvertPath(String packageName) {
		return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
	}
	
	/**
	 * 初始化配置信息
	 */
	private static void init() {
		Properties prop = loadProperties();
		
		JDBC_URL = prop.getProperty("jdbc.url");
		JDBC_USERNAME = prop.getProperty("jdbc.username");
		JDBC_PASSWORD = prop.getProperty("jdbc.password");
		JDBC_DRIVER_CLASS_NAME = prop.getProperty("jdbc.driver.class.name");
		
		JAVA_PATH = prop.getProperty("java.path");
		RESOURCES_PATH = prop.getProperty("resources.path");
		TEMPLATE_FILE_PATH = PROJECT_PATH + prop.getProperty("template.file.path");
		
		BASE_PACKAGE = prop.getProperty("base.package");
		MODEL_PACKAGE = prop.getProperty("model.package");
		MAPPER_PACKAGE = prop.getProperty("mapper.package");
		SERVICE_PACKAGE = prop.getProperty("service.package");
		SERVICE_IMPL_PACKAGE = prop.getProperty("service.impl.package");
		CONTROLLER_PACKAGE = prop.getProperty("controller.package");
		
		MAPPER_INTERFACE_REFERENCE = prop.getProperty("mapper.interface.reference");
		SERVICE_INTERFACE_REFERENCE = prop.getProperty("service.interface.reference");
		ABSTRACT_SERVICE_CLASS_REFERENCE = prop.getProperty("abstract.service.class.reference");
		
		String servicePackage = prop.getProperty("package.path.service");
		String serviceImplPackage = prop.getProperty("package.path.service.impl");
		String controllerPackage = prop.getProperty("package.path.controller");
		
		PACKAGE_PATH_SERVICE = "".equals(servicePackage) ? packageConvertPath(SERVICE_PACKAGE) : servicePackage;
		PACKAGE_PATH_SERVICE_IMPL = "".equals(serviceImplPackage) ? packageConvertPath(SERVICE_IMPL_PACKAGE) : serviceImplPackage;
		PACKAGE_PATH_CONTROLLER = "".equals(controllerPackage) ? packageConvertPath(CONTROLLER_PACKAGE) : controllerPackage;
		
		AUTHOR = prop.getProperty("author");
		String dateFormat = "".equals(prop.getProperty("date-format")) ? "yyyy/MM/dd" : prop.getProperty("date-format");
		DATE = new SimpleDateFormat(dateFormat).format(new Date());
	}
	
	/**
	 * 加载配置文件
	 * @return
	 */
	private static Properties loadProperties() {
		Properties prop = null;
		try {
			prop = new Properties();
			InputStream in = CodeGeneratorManager.class.getClassLoader().getResourceAsStream("generatorConfig.properties");
			prop.load(in);
		} catch (Exception e) {
			throw new RuntimeException("加载配置文件异常!", e);
		}
		return prop;
	}
	
}
