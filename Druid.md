## Druid介绍
`Druid`是**阿里巴巴**的一个开源项目，号称为监控而生的数据库连接池，在功能、性能、扩展性方面都超过其他例如`DBCP`、`C3P0`、`BoneCP`、`Proxool`、`JBoss`、`DataSource`等连接池，而且`Druid`已经在阿里巴巴部署了超过600个应用，通过了极为严格的考验，这才收获了大家的青睐！
## Spring boot配置Druid
### 添加依赖
在我们项目的`pom.xml`文件中添加如下的依赖：
```xml
<!-- druid -->
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>druid-spring-boot-starter</artifactId>
   <version>1.1.10</version>
</dependency>
```
**这里需要注意的是(很多人出错的原因)，`Druid`还有另外一种依赖如下：**
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.3</version>
</dependency>
```
```
两种依赖都可以，但是配置的方式有些不同，使用下面的这种依赖项，在配置的时候需要新建`Druid`的配置类文件，而第一种依赖项则不需要，这里使用的就是第一种--更简单。
```
### 添加配置
1. 首先在项目的配置文件`application.properties`文件中添加如下内容，表明使用`Druid`连接池：
```xml
#表明使用Druid连接池
spring.database.type=com.alibaba.druid.pool.DruidDataSource
```
2. 配置数据源信息(整合Mybatis的时候已经说过了，为了连贯性这里再提一遍)
```xml
#配置实体类的位置
mybatis.type-aliases-package=com.web.springbootmybatis.entity
#xml文件位置
mybatis.mapper-locations=classpath:mapper/*.xml

#mysql数据库连接信息配置
#mysql驱动
spring.datasource.driverClassName=com.mysql.jdbc.Driver
#数据库连接信息
spring.datasource.url=jdbc:mysql://localhost:3306/eran?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
#数据库用户名
spring.datasource.username=root
#数据库密码
spring.datasource.password=root
```
3. 接下来配置**连接池**的相关属性,这些参数根据自己的需要灵活配置即可：
```xml
#初始化时建立物理连接的个数。
spring.datasource.druid.initial-size=5
#最大连接池数量
spring.datasource.druid.max-active=20
#最小连接池数量
spring.datasource.druid.min-idle=5
#获取连接时最大等待时间，单位毫秒
spring.datasource.druid.max-wait=3000
#是否缓存preparedStatement，也就是PSCache,PSCache对支持游标的数据库性能提升巨大，比如说oracle,在mysql下建议关闭。
spring.datasource.druid.pool-prepared-statements=false
#要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
spring.datasource.druid.max-open-prepared-statements= -1
#配置检测可以关闭的空闲连接间隔时间
spring.datasource.druid.time-between-eviction-runs-millis=60000
# 配置连接在池中的最小生存时间
spring.datasource.druid.min-evictable-idle-time-millis= 300000
spring.datasource.druid.max-evictable-idle-time-millis= 400000
```
4. 通过别名的方式配置扩展插件，常用的插件有：  

| 别名 | 含义 |
| ---- | ---- |
| stat | 监控统计 |
| log4j	| 日志 |
| wall | 防御sql注入 |  

这里我们就配置了`stat`和`wall`,配置多个英文逗号分隔，配置如下：
```xml
#监控统计的stat,以及防sql注入的wall
spring.datasource.druid.filters= stat,wall
#Spring监控AOP切入点，如x.y.z.service.*,配置多个英文逗号分隔
spring.datasource.druid.aop-patterns= com.web.springbootdruid.service.*
```
> 这里配置很重要，比如不配置`stat`,我们在`Druid`的监控页面中就拿不到想要的信息。
5. 上边我们开启`stat`监控统计插件，下边进行监控配置：
```xml
#是否启用StatFilter默认值true
spring.datasource.druid.web-stat-filter.enabled= true
#添加过滤规则
spring.datasource.druid.web-stat-filter.url-pattern=/*
#忽略过滤的格式
spring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*
```
6. StatViewServlet配置：Druid内置提供了一个StatViewServlet用于展示Druid的统计信息，StatViewServlet的用途包括：
- 提供监控信息展示的html页面
- 提供监控信息的JSON API
配置如下，需要注意的是用户名和密码：
```xml
#是否启用StatViewServlet默认值true
spring.datasource.druid.stat-view-servlet.enabled= true
#访问路径为/druid时，跳转到StatViewServlet
spring.datasource.druid.stat-view-servlet.url-pattern=/druid/*
# 是否能够重置数据
spring.datasource.druid.stat-view-servlet.reset-enable=false
# 需要账号密码才能访问控制台，默认为root
spring.datasource.druid.stat-view-servlet.login-username=druid
spring.datasource.druid.stat-view-servlet.login-password=druid
#IP白名单
spring.datasource.druid.stat-view-servlet.allow=127.0.0.1
#&emsp;IP黑名单（共同存在时，deny优先于allow）
spring.datasource.druid.stat-view-servlet.deny=
```
到这里，关于`Druid`的配置就基本完成了，想要了解，可以去查阅官方文档`https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter`
## 验证Druid
我们直接启动项目，浏览器访问`localhost:8080/druid`或者`127.0.0.1:8080/druid`得到如下页面说明配置成功：  
![druid](https://user-gold-cdn.xitu.io/2019/1/17/1685c05b273b155b?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)  
进入`Druid`的登陆页面，输入我们在配置文件中配置的用户名和密码登陆，进入下面的页面：  
![druid](https://user-gold-cdn.xitu.io/2019/1/17/1685c05b28192b11?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)  
首页可以看到一些基本配置信息，点击菜单栏中的数据源可以查看到我们的数据源配置信息，以及连接池配置信息：  
![druid](https://user-gold-cdn.xitu.io/2019/1/17/1685c05b282f1293?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
点击`sql监控`，查看执行`sql`信息：
![druid](https://user-gold-cdn.xitu.io/2019/1/17/1685c05b2842b131?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)  
点击该条记录可以查看更多详情，这里就不介绍了，配置成功的小伙伴们可以自己体验`Druid`的各种强大功能！
