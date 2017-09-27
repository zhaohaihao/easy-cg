# Java代码生成器
## 介绍
这是一个基于数据库表<br/>用来自动生成 Model & Mapper & Service & ServiceImpl & Controller 等代码的代码生成器<br/>
使用者可以通过修改ftl模板来生成自己所需的基本代码块<br/>
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
|						|——CodeGeneratorManager.java // 代码生成器基础项 (通用方法)
	|
|						|——CodeGeneratorConfig.java // 所有的配置信息变量
	|
|						|——CodeGenerator.java // 主要逻辑接口
	|
|		|	|	|	|	|——impl
	|
|							|——ControllerGenerator.java // Controller层 代码生成器
	|
|							|——ServiceGenerator.java // Service层 代码生成器
	|
|							|——ModelAndMapperGenerator.java // Model&Mapper 代码生成器
	|
	|
|		|	|	|	|——util
	|
|						|——StringUtils.java // 字符串操作常用方法集
	|
|		|——resource
	|
|		|	|——generatorConfig.properties // 配置文件
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
#### 运行
进入到 `src/test/java` 目录下<br />
找到`CodeGeneratorMain`类 为生成器的启动项<br />
直接 `Run As Java Application` 运行即可<br />

#### 修改配置
进入到 `src/test/resources` 目录下<br />
找到 `generatorConfig.properties` 文件<br />
修改对应的参数即可<br />
具体的注释信息可参考 `/src/test/java/com/codegen/service/CodeGeneratorConfig.java` 类<br />

#### Mybatis 通用插件
新增 Mapper 通用插件&分页插件<br />
已经固定放置 `src/main/java/com/bigsea/sns/dao` 和 <br />
`src/main/java/com/bigsea/sns/service` 两个包下<br />
使用者可以根据自已定义的路径存放<br/>
但是需要注意的是 `MyMapper` 接口存放的路径最好不要被 `Mybatis` 扫描到, 会出现异常<br />
插件路径变换后, 需要修改对应配置文件的值<br />

#### 入口说明
以表名 gen_test_demo 为例子, 主要是以下几种情况:<br/>
- gen_test_demo ==> Demo 可以传入多表<br/>
genCodeWithSimpleName("gen_test_demo");<br/>
- gen_test_demo ==> GenTestDemo 可以传入多表<br/>
genCodeWithDetailName("gen_test_demo");<br/>
- gen_test_demo ==> IDemo 自定义名称<br/>
genCodeWithCustomName("gen_test_demo", "IDemo");<br/>

#### 模板样式修改
如果需要生成自己所需的 Controller & Service & ServiceImpl 样式<br/>
进入到 `src/test/resources/generator/template` 目录下<br />
修改对应的ftl文件即可

#### 数据库表名规则
推荐表名的格式类似 `gen_test_userinfo` 需要下划线分割<br />
`gen` 作为项目别名<br />
`test` 作为区分字段, 用于分包<br />
`userinfo` 可作为实体类名<br />
当然表名也可以为 `gen_test_user_info` 与 `gen_test_userinfo` 类似<br />

如果表名有自己的规则, 您可以通过修改 `/src/test/java/com/codegen/service/CodeGeneratorManager.java` 类中<br />
`getSign(String tableName)`, <br />
`getDefModelName(String tableName)`, <br />
`getTableNameSplit(String tableName)`, <br />
这三个方法来自定义规则

#### 使用过程遇到问题
使用过程仍存在相关Bug<br />
您可以将详情发送至我的邮箱<a href="mailto:bigsea1994@gmail.com">bigsea1994@gmail.com</a><br />
万分感激

## 相关参考文档
- MyBatis（[查看官方中文文档](http://www.mybatis.org/mybatis-3/zh/index.html)）
- MyBatis通用Mapper插件（[查看官方中文文档](https://mapperhelper.github.io/docs/)）
- MyBatis PageHelper分页插件（[查看官方中文文档](https://pagehelper.github.io/docs/)）

<br /><br />

## 加入我们一起交流学习吧!
<div class="text-center">
	Github交流群：
	<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=fc6d021a1e1d1155847180863178d3b8111783f33abf6cfda0efe998e209a454"><img border="0" src="https://github.com/zhaohaihao/Java-Design-Patterns/blob/master/group.png" alt="Github交流群" title="Github交流群"></a>
</div>