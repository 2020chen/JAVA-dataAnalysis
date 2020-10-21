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
- 进入tomcat接口地址<kbd>//localhost:8080/</kbd>，实现效果
## 分析运行原理
1. pom.xml
- 父依赖  
- 启动器   
  场景启动器 springboot-boot-starter-xxx  
  web模块运行所需的组件 spring-boot-start-web  
  获取候选的配置
```java
protected List<String> getCandidateConfigrations(AnnotationMetadata metadata,AnnotationAttributes){
  List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoryClass(),getBeanClassLoader());
  Assert.notEmpty(configurations,"No auto configuration classes found in META-INF/spring.factories.If you"+"are using a custom packaging,make sure that file is correct.");
  return configurations;
}
```
2. 主启动类  
1. springboot在启动的时候，从类路径下/META-INF/spring.factories获取指定的值；
2. 将这些自动配置的类导入容器，自动配置就会生效，帮我进行自动配置！
3. 整合javaEE,解决方案和自动配置的东西都在spring-boot-autoconfigure-2.2.0.RELEASE.jar这个包下
4. 把所有需要导入的组件，以类名的方式返回，这些组件就会被添加到容器；
5. 容器中也会存在非常多的xxxAutoConfiguration的文件（@Bean），就是这些类给容器中导入了这个场景需要的所有组件，并自动配置，@Configuration,JavaConfig!
![run](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7L1vFQMnaRIJSmeZ58T2eZicjafiawQLp9u8wc4ic1Mjy6OyfibzfjVofeL5pnS1NSFKVjlIg6neI9ySg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
