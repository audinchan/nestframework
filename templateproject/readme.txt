一、项目初始化工作

1. 预备步骤
      将templateproject目录改名为项目小写名称

2. 打开命令行，并切换当前目录至项目目录
   在命令行中执行”init.bat 包前缀 项目小写名称“
      如 init.bat com.yourcompany demo
   为了避免以后错误执行init.bat，执行完此步骤可删除init.bat文件。
      
3. 【从这里开始都可以在Eclpise环境中操作】
      使用Eclipse的菜单File->Import，
    然后选择General->Existing projects into workspace，将项目导入Eclipse环境。

4. 打开build.properties文件
   修改数据库配置

5. 依次执行ant任务【打开Eclipse的Ant视图，将项目中的build.xml拖拽到其中】
   genconf (生成数据库和Hibernate配置文件)
   genmapping (生成Hibernate映射文件)
   genpojo(或genpojo2) (生成Hibernate实例类文件)
   gencode(或gencode2) (生成基础代码)
   
   或者直接执行 genall(或genall2) 完成所有步骤
     【其中括号里的是生成annotation驱动的代码，annotation驱动的代码不需要执行genmapping】
   
二、部署注意事项

1. applicationContext-service.xml中的hibernate.show_sql改为false；
2. 去掉p6spy调试sql（如果使用了）；
3. 运行稳定后，关闭调试。
