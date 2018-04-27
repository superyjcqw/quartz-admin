## 简易Quartz集群管理工具

> 比较了几市面上几款调度框架，发现比较好的还是quartz与elastic-job，elastic-job虽支持分布式调度，但才出来不久还不成熟，而quartz是调度界元老，经过市场考验，能满足我们目前的需求，于是选择用它。由于我们集群比较多，所以开发了一个统一的调度平台，实现了调度与执行的分离，解决了可视化、易配置及监控告警功能。

#### 1. 功能介绍
- **支持已有quartz集群接入**
- **支持动态添加数据源**
- **支持动态管理job**
- **能方便查看调度日志、正在运行的任务**
- **支持异常job监控报警**
- **支持慢执行job监控报警**
- **支持到点未执行job监控报警**

#### 2. 所用技术 

- **springboot + mysql + JdbcTemplate + thymeleaf++shiro**

#### 3. 如何接入

1.  **下载源码**
2.  **配置application.yml内数据源、邮件等**
3.  **执行resource/sql内quartz-admin.sql文件初始化数据库，默认用户名admin,密码112233**
4.  **main方法启动Application类**
5.  **quartz执行端[(demo传送门)](https://github.com/superyjcqw/scheduler)提供job管理的http接口供平台调用**
6.  **quartz执行端[(demo传送门)](https://github.com/superyjcqw/scheduler)把调度日志插入平台数据库**
7.  **登录系统在集群管理中添加quartz集群(集群名作为集群唯一标识，最好不要修改)，远程调用节点使用步骤5中的地址**
8.  **配置报警人，每个job可独立配置报警人，若不配置默认发给第一个报警人**

#### 4. 功能截图
![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminDA441A1E-D6FC-4C5E-B9E3-4DCE71E56DEF.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-173900.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174037.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174005.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174127.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174141.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174212.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174223.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174244.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174254.png)

![image](http://7u2fcj.com1.z0.glb.clouddn.com/quartz-adminWX20170822-174414.png)






