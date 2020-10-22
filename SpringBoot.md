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
##Restful api原理
1. Lombok 优化代码利器
2. `@RestController` 
3. `@RequestParam`以及`@Pathvairable`
4. `@RequestMapping`、` @GetMapping`......
5. `Responsity`
### 下载 Lombok 优化代码利器
```xml
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.10</version>
		</dependency>
```
同时需要下载 IDEA 中支持 lombok 的插件：

![ IDEA 中下载支持 lombok 的插件](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-7/lombok-idea.png)
### RESTful Web 服务开发
假如我们有一个书架，上面放了很多书。为此，我们需要新建一个 `Book` 实体类。

`com.example.helloworld.entity`

```java
/**
 * @author shuang.kou
 */
@Data
public class Book {
    private String name;
    private String description;
}
```

我们还需要一个控制器对书架上进行添加、查找以及查看。为此，我们需要新建一个 `BookController` 。

```java
import com.example.helloworld.entity.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {

    private List<Book> books = new ArrayList<>();

    @PostMapping("/book")
    public ResponseEntity<List<Book>> addBook(@RequestBody Book book) {
        books.add(book);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity deleteBookById(@PathVariable("id") int id) {
        books.remove(id);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/book")
    public ResponseEntity getBookByName(@RequestParam("name") String name) {
        List<Book> results = books.stream().filter(book -> book.getName().equals(name)).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}
```

1. `@RestController`  **将返回的对象数据直接以 JSON 或 XML 形式写入 HTTP 响应(Response)中。**绝大部分情况下都是直接以  JSON 形式返回给客户端，很少的情况下才会以 XML 形式返回。转换成 XML 形式还需要额为的工作，上面代码中演示的直接就是将对象数据直接以 JSON 形式写入 HTTP 响应(Response)中。关于`@Controller`和`@RestController` 的对比，我会在下一篇文章中单独介绍到（`@Controller` +`@ResponseBody`= `@RestController`）。
2. `@RequestMapping` :上面的示例中没有指定 GET 与 PUT、POST 等，因为**`@RequestMapping`默认映射所有HTTP Action**，你可以使用`@RequestMapping(method=ActionType)`来缩小这个映射。
3. `@PostMapping`实际上就等价于 `@RequestMapping(method = RequestMethod.POST)`，同样的 ` @DeleteMapping` ,`@GetMapping`也都一样，常用的 HTTP Action 都有一个这种形式的注解所对应。
4. `@PathVariable` :取url地址中的参数。`@RequestParam ` url的查询参数值。
5. `@RequestBody`:可以**将 *HttpRequest* body 中的 JSON 类型数据反序列化为合适的 Java 类型。**
6. `ResponseEntity`: **表示整个HTTP Response：状态码，标头和正文内容**。我们可以使用它来自定义HTTP Response 的内容。
