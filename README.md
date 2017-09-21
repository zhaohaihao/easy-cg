# Java代码生成器
## 介绍
这是一个基于数据库表<br/>用来自动生成 Model & Mapper & Service & ServiceImpl & Controller 等代码的代码生成器<br/>
使用者可以通过修改Ftl模板来生成自己所需的基本代码块<br/>
主要是为了解决日常工作、练习中代码的重复工作量<br/>
目前仅支持 Mybatis 底层代码的生成

## 目录结构
```
|——src	
	|——main                                             
|		|——java					// 存放生成的代码
	|
|		|——resource
	|
|		|	|——mapper			// 存放生成的 Mapper.xml 文件
	|
|
	|——test
|		|——java					// 存放生成器的代码
	|
|		|	|——com
	|
|		|	|	|——codegen
	|
|		|	|	|	|——main
	|
|						|——CodeGeneratorMain.java // 代码生成器启动项
	|
|		|	|	|	|——service
	|
|						|——CodeGeneratorManager.java // 代码生成器基础项 (常量信息 & 通用方法)
	|
|						|——CodeGenerator.java // 主要逻辑接口
	|
|		|	|	|	|	|——impl
	|
|							|——ControllerGenerator.java // Controller层 代码生成器
	|
|							|——ServiceGenerator.java // Service层 代码生成器
	|
|							|——ModelAndMapperGenerator.java // Model & Mapper 代码生成器
	|
	|
|		|	|	|	|——util
	|
|						|——StringUtils.java // 字符串操作常用方法集
	|
|		|——resource
	|
|		|	|——generator
	|
|		|	|	|——template				// 存放ftl模板代码
	|		
|					|——controller.ftl	// Controller 模板代码
	|		
|					|——service.ftl		// Service 模板代码
	|		
|					|——service-impl.ftl	// ServiceImpl 模板代码
```

## 使用说明
1.  进入到 `src/test/java` 目录下<br />
　　找到`CodeGeneratorMain`类 为生成器的启动项<br />
　　直接 `Run As Java Application` 运行即可<br />
2.  如果需要修改数据库,或者存储路径相关的配置<br>
　　找到`CodeGeneratorManager`类,在其中修改静态即可,后期将会调整至配置文件中<br />
3. 　新增 Mapper 通用插件&分页插件<br />
　　已经固定放置 `/CodeGenerator/src/main/java/com/bigsea/sns/dao` 和 `/CodeGenerator/src/main/java/com/bigsea/sns/service` 两个包下<br />
　　使用者可以根据自已定义的路径存放<br/>
　　需要注意的是 `MyMapper` 接口存放的路径最好不要被 Mybatis 扫描到, 会出现异常<br />
4.  目前提供三个入口:<br/>
　　以表名 gen_test_demo 为例子, 主要是以下几种情况:<br/>
　　1. 　gen_test_demo ==> Demo 可以传入多表<br/>
　　　　genCodeWithSimpleName("gen_test_demo");<br/>
 		
　　2.  gen_test_demo ==> GenTestDemo 可以传入多表<br/>
　　　　genCodeWithDetailName("gen_test_demo");<br/>
	 
　　3.  gen_test_demo ==> IDemo 自定义名称<br/>
　　　　genCodeWithCustomName("gen_test_demo", "IDemo");<br/>

5.  使用过程仍存在相关Bug<br />
　　您可以将详情发送至我的邮箱<a href="mailto:bigsea1994@gmail.com">bigsea1994@gmail.com</a><br />
　　万分感激

<br /><br />

## 加入我们一起交流学习吧!
<div class="text-center">
	Github交流群：
	<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=fc6d021a1e1d1155847180863178d3b8111783f33abf6cfda0efe998e209a454"><img border="0" src="https://github.com/zhaohaihao/Java-Design-Patterns/blob/master/group.png" alt="Github交流群" title="Github交流群"></a>
</div>