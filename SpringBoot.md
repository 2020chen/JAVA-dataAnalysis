> 本周目标：能用Springboot框架编写简单Java接口  
> 1、了解Springboot框架结构  
> 2、了解常用注解  
> 3、编写Springboot路由
## 创建SpringBoot项目
1. 官网创建
- 输入项目名称，选择General创建Demo
2. IDEA创建
- 新建项目，选择Spring initalizr，填写项目名称，进行创建
3. 编写http接口
- 新建controller包在主程序的同级目录，编写以下代码文件进行编译
```java
@RestController
public class HelloController{
  @RequsetMapping("/hello")
  public string hello(){
    return "hello world";
  }  
}
```
- 进入tomcat接口地址//localhost:8080/，实现效果
## 分析运行原理
1. pom.xml
- 父依赖
- 启动器   
  场景启动器 springboot-boot-starter-xxx  
  web模块运行所需的组件 spring-boot-start-web
2. 主启动类
![run](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7L1vFQMnaRIJSmeZ58T2eZicjafiawQLp9u8wc4ic1Mjy6OyfibzfjVofeL5pnS1NSFKVjlIg6neI9ySg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
