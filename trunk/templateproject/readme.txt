一、项目初始化工作

1. 将templateproject目录改名为项目小写名称。

2. 双机运行init.bat文件。
	执行完此步骤删除init.bat文件。
      
3. 使用Eclipse的菜单File->Import，
	然后选择General->Existing Projects into Workspace，将项目导入Eclipse环境。

4. 编辑build.properties文件
	修改数据库配置（db.*和hibernate.dialect属性需要修改）

5. 打开Eclipse的Ant视图(Window->Show View->Ant)，将build.xml拖拽到Ant视图中。
	展开Ant任务，执行genall生成各种配置和代码
	
	或者依次执行以下Ant任务【可选】
		genconf (生成数据库和Hibernate配置文件)
		genmapping (生成Hibernate映射文件)
		genpojo (生成Hibernate实例类文件)
		gencode (生成基础代码)
		
二、代码生成注意事项

1. 数据库表必须有唯一主键（多对多关系中间表除外）。
2. 多对多关系中间表只能有两个外键字段，分别指向关系两端表的主键，这两个字段同时作为联合主键。
3. 默认自动检测主键生成机制，如需指定，则修改conf/hibernate.reveng.xml文件。
4. 如需要排除系统表，则修改conf/hibernate.reveng.xml文件。
   
三、部署注意事项

1. 将applicationContext-service.xml中的hibernate.show_sql改为false。
2. 去掉p6spy调试sql（如果使用了）。
3. 运行稳定后，关闭调试。
