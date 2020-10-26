## JDBC 简介
JDBC 的全称是 Java Database Connectivity，叫做 Java 数据库连接。它包括了一组与数据库交互的 api，还有与数据库进行通信的驱动程序。  
我们要写涉及到数据库的程序，是通过 C 语言或者 C++ 语言直接访问数据库的接口，如下图所示。
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1192timestamp1437293494988.png)  
对于不同的数据库，我们需要知道不同数据库对外提供的系统 API，这就影响了我们程序的扩展和跨平台的实现。
对不同的数据库接口进行统一需要分层的思想。我们只需要和最上层接口进行交互，剩下的部分就交给其他各层去处理，我们的任务就变的轻松简单许多。
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1192timestamp1437293526322.png)  
在 Java 上面只有一种数据库连接统一接口——JDBC。JDBC 为数据库开发人员提供了一个标准的 API，据此可以构建更高级的工具和接口使数据库开发人员能够用纯 Java API 编写数据库应用程序。  
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1192timestamp1437293582121.png)  
## JDBC SQL 语法
对于数据库的操作我们需要结构化查询语句 SQL
1. 创建数据库
CREATE DATABASE 语句用于创建一个新的数据库。语法是：
```sql
SQL> CREATE DATABASE DATABASE_NAME;
```
例子，创建一个名为 EXAMPLE 数据库：
```sql
SQL> CREATE DATABASE EXAMPLE;
```
2. 删除数据库
使用 DROP DATABASE 语句用于删除现有的数据库。语法是：
```sql
SQL> DROP DATABASE DATABASE_NAME;
```
注意：要创建或删除，应该有数据库服务器上管理员权限。请注意，删除数据库将损失所有存储在数据库中的数据。

例子，删除我们刚刚建好的数据库：
```sql
SQL> DROP DATABASE EXAMPLE;
```
3. 创建表
CREATE TABLE 语句用于创建一个新表。语法是：
```sql
SQL> CREATE TABLE table_name
(
   column_name column_data_type,
   column_name column_data_type
   ...
);
```
例子，下面的 SQL 语句创建一个有四个属性的 Students 表：
```sql
SQL> CREATE TABLE Students
(
   id INT NOT NULL,
   age INT NOT NULL,
   name VARCHAR(255),
   major VARCHAR(255),
   PRIMARY KEY ( id )
);
```
4. 删除表
DROP TABLE 语句用于删除现有的表。语法是：
```sql
SQL> DROP TABLE table_name;
```
例子，下面的 SQL 语句删除一个名为 Students 表：
```sql
SQL> DROP TABLE Students;
```
5. 插入数据
语法 INSERT 如下，其中 column1, column2 ，依此类推的属性值：
```sql
SQL> INSERT INTO table_name VALUES (column1, column2, ...);
```
例子，下面的 INSERT 语句中插入先前创建的 Students 表：
```sql
SQL> INSERT INTO Students VALUES (1, 18, 'Mumu', 'Java');
```
6. 查询数据
SELECT 语句用于从数据库中检索数据。该语法的 SELECT 是：
```sql
SQL> SELECT column_name, column_name, ...
     FROM table_name
     WHERE conditions;
```
WHERE 子句可以使用比较操作符例如 =, !=, <, >, <=, >=, 以及 BETWEEN 和 LIKE 等操作符。

例子，下面的 SQL 语句从 Students 表选择 id 为 1 的学生，并将该学生的姓名和年龄显示出来：
```sql
SQL> SELECT name, age
     FROM Students
     WHERE id = 1;
```
下面的 SQL 语句从 Students 表中查询姓名中有 om 字样的学生，并将学生的姓名和专业显示出来：
```sql
SQL> SELECT name, major
     FROM Students
     WHERE name LIKE '%om%';
```
7. 更新数据
UPDATE 语句用于更新数据。UPDATE 语法为：
```sql
SQL> UPDATE table_name
     SET column_name = value, column_name = value, ...
     WHERE conditions;
```
例子，下面的 SQL 的 UPDATE 语句表示将 ID 为 1 的学生的 age 改为 20：
```sql
SQL> UPDATE Students SET age=20 WHERE id=1;
```
8. 删除数据
DELETE 语句用来删除表中的数据。语法 DELETE 是：
```sql
SQL> DELETE FROM table_name WHERE conditions;
```
例子，下面的 SQL DELETE 语句删除 ID 为 1 的学生的记录：
```sql
SQL> DELETE FROM Students WHERE id=1;
```
## JDBC 环境设置
1. 启动Mysql服务器
```sql
sudo service mysql start
```
2. 连接与断开数据库  
连接数据库（本地密码环境为空）：
```sql
mysql -u root
```
退出数据库：
```sql
mysql> exit
```
## 创建 JDBC 应用程序
学习如何编写一个真正的 JDBC 程序。我们先来浏览一下它的步骤，然后我们在后面的代码中作详细地讲解：

1. 导入 JDBC 驱动 打开 terminal，输入命令获取驱动包
```xml
wget https://labfile.oss.aliyuncs.com/courses/110/mysql-connector-java-5.1.47.jar
```
有了驱动就可以与数据库打开一个通信通道  
2. 打开连接：需要使用 `DriverManager.getConnection()`方法创建一个`Connection`对象，它代表与数据库的物理连接

3. 执行查询：需要使用类型声明的对象建立并提交一个 SQL 语句到数据库

4. 从结果集中提取数据：要求使用适当的关于 `ResultSet.getXXX()` 方法来检索结果集的数据

5. 处理结果集：对得到的结果集进行相关的操作

6. 清理环境：需要明确地关闭所有的数据库资源，释放内存

例子：

1、先创建数据库和相应的内容：
```sql
sudo service mysql start
mysql -u root
create database EXAMPLE;
use EXAMPLE
```
![sql](https://doc.shiyanlou.com/document-uid441493labid8432timestamp1542595008402.png)  
```sql
create table Students
(
id int not null,
age int not null,
name varchar(255),
primary key(id)
);
insert into Students values(1,18,'Tom'),
(2,20,'Aby'),(4,20,'Tomson');
```
![sql](https://doc.shiyanlou.com/document-uid441493labid8432timestamp1542595044618.png)  

2、java 程序访问数据库

我们在project目录下创建文件JdbcTest.java
```java
import java.sql.*;

public class JdbcTest {
   // JDBC 驱动器名称 和数据库地址
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   //数据库的名称为 EXAMPLE
   static final String DB_URL = "jdbc:mysql://localhost/EXAMPLE";

   //  数据库用户和密码
   static final String USER = "root";

   static final String PASS = "";

   public static void main(String[] args) {
       Connection conn = null;
       Statement stmt = null;
       try{
           //注册 JDBC 驱动程序
           Class.forName("com.mysql.jdbc.Driver");

           //打开连接
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);

           //执行查询
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           sql = "SELECT id, name, age FROM Students";
           ResultSet rs = stmt.executeQuery(sql);

           //得到和处理结果集
           while(rs.next()){
               //检索
               int id  = rs.getInt("id");
               int age = rs.getInt("age");
               String name = rs.getString("name");

               //显示
               System.out.print("ID: " + id);
               System.out.print(", Age: " + age);
               System.out.print(", Name: " + name);
               System.out.println();
           }
           //清理环境
           rs.close();
           stmt.close();
           conn.close();
       }catch(SQLException se){
           // JDBC 操作错误
           se.printStackTrace();
       }catch(Exception e){
           // Class.forName 错误
           e.printStackTrace();
       }finally{
           //这里一般用来关闭资源的
           try{
               if(stmt!=null)
                   stmt.close();
           }catch(SQLException se2){
           }
           try{
               if(conn!=null)
                   conn.close();
           }catch(SQLException se){
               se.printStackTrace();
           }
       }
       System.out.println("Goodbye!");
   }
}
```
3、最后我们来看看结果吧：

使用命令编译并运行
```sql
javac -cp .:mysql-connector-java-5.1.47.jar JdbcTest.java
java -cp .:mysql-connector-java-5.1.47.jar JdbcTest
```
![img](https://doc.shiyanlou.com/document-uid441493labid8432timestamp1542596266804.png)  
## JDBC 基础
1. JDBC结构  
JDBC API 是 Java 开发工具包 (JDK) 的组成部份，由三部分组成：

JDBC 驱动程序管理器  
JDBC 驱动程序测试工具包  
JDBC-ODBC 桥  
a. JDBC 驱动程序管理器是JDBC体系结构的支柱，其主要作用是把Java应用程序连接到正确的JDBC驱动程序上。

b. JDBC 驱动程序测试工具包为 JDBC 驱动程序的运行提供一定的可信度，只有通过 JDBC 驱动程序测试包的驱动程序才被认为是符合 JDBC 标准的。

c. JDBC-ODBC 桥使ODBC驱动程序可被用作JDBC驱动程序。其目标是为方便实现访问某些不常见的DBMS（数据库管理系统）, 它的实现为JDBC的快速发展提供了一条途径。

JDBC 既支持数据库访问的两层模型，也支持三层模型。

1、数据库访问的两层模型  
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1193timestamp1437355574896.png)  
2、数据库访问的三层模型  
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1193timestamp1437355655105.png)  
2. JDBC驱动类型  
JDBC 驱动程序实现 JDBC API 中定义的接口，用于与数据库服务器进行交互。JDBC 驱动程序可以打开数据库连接，并通过发送 SQL 或数据库命令，然后在收到结果与 Java 进行交互。

JDBC 驱动程序的实现因为各种各样的操作系统和 Java 运行在不同的硬件平台上而不同。JDBC 驱动类型可以归结为以下几类：

- JDBC-ODBC 桥接 ODBC 驱动程序：它是将 JDBC 翻译成 ODBC, 然后使用一个 ODBC 驱动程序与数据库进行通信。当 Java 刚出来时，这是一个有用的驱动程序，因为大多数的数据库只支持 ODBC 访问，但现在建议使用此类型的驱动程序仅用于实验用途或在没有其他选择的情况（注意：从 JDK1.8 开始 ODBC 已经被移除）。  
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1193timestamp1437356554087.png)  
- 本地 API 用 Java 来编写的驱动程序：这种类型的驱动程序把客户机 API 上的 JDBC 调用转换为 Oracle、Sybase、 Informix、DB2 或其它 DBMS 的调用。  
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1193timestamp1437356877667.png)  
- JDBC 网络纯 Java 驱动程序：这种驱动程序将 JDBC 转换为与 DBMS 无关的网络协议，这是最为灵活的 JDBC 驱动程序。它是一个三层的方法来访问数据库，在 JDBC 客户端使用标准的网络套接字与中间件应用服务器进行通信。然后由中间件应用服务器进入由 DBMS 所需要的的调用格式转换，并转发到数据库服务器。  
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1193timestamp1437357558698.png)  
- 本地协议纯 Java 驱动程序：这种类型的驱动程序将 JDBC 调用直接转换为 DBMS 所使用的专用网络协议。是 Intranet 访问的一个很实用的解决方法。它是直接与供应商的数据库进行通信，通过 socket 连接一个纯粹的基于 Java 的驱动程序。这是可用于数据库的最高性能的驱动程序，并且通常由供应商本身提供。  
![jdbc](https://doc.shiyanlou.com/document-uid79144labid1193timestamp1437357842529.png)  
MySQL 的 Connector/Java 的驱动程序是一个类型 4 驱动程序。因为他们的网络协议的专有性的，数据库厂商通常提供类型 4 的驱动程序。

通常情况下如果正在访问一个类型的数据库，如 Oracle，Sybase 或 IBM，首选驱动程序是类型 4。

如果 Java 应用程序同时访问多个数据库类型，类型 3 是首选的驱动程序。

第 2 类驱动程序是在类型 3 或类型 4 驱动程序还没有提供数据库的情况下显得非常有用。

类型 1 驱动程序不被认为是部署级别的驱动程序，它通常仅用于开发和测试目的。  
3. JDBC连接数据库  
涉及到建立一个 JDBC 连接的编程主要有四个步骤：

- 导入 JDBC 驱动： 只有拥有了驱动程序我们才可以注册驱动程序完成连接的其他步骤。

- 注册 JDBC 驱动程序：这一步会导致 JVM 加载所需的驱动类实现到内存中，然后才可以实现 JDBC 请求。

- 数据库 URL 指定：创建具有正确格式的地址，指向到要连接的数据库。

- 创建连接对象：最后，代码调用 DriverManager 对象的 getConnection() 方法来建立实际的数据库连接。

接下来我们便详细讲解这四步。  
3.1 导入 JDBC 驱动  
3.2 注册 JDBC 驱动程序  
我们在使用驱动程序之前，必须注册你的驱动程序。注册驱动程序的本质就是将我们将要使用的数据库的驱动类文件动态的加载到内存中，然后才能进行数据库。比如我们使用的 Mysql 数据库。我们可以通过以下两种方式来注册我们的驱动程序。

1、方法 1——Class.forName()：

动态加载一个类最常用的方法是使用 Java 的 Class.forName() 方法，通过使用这个方法来将数据库的驱动类动态加载到内存中，然后我们就可以使用。

使用 Class.forName() 来注册 Mysql 驱动程序：
```java
try {
   Class.forName("com.mysql.jdbc.Driver");
}
catch(ClassNotFoundException ex) {
   System.out.println("Error: unable to load driver class!");
   System.exit(1);
}
```
2、方法 2——DriverManager.registerDriver()：
```java
   Driver driver = new com.mysql.jdbc.Driver();
   DriverManager.registerDriver(driver);
```
3.3 指定数据库连接 URL  
当加载了驱动程序，便可以使用 DriverManager.getConnection() 方法连接到数据库了。

这里给出 DriverManager.getConnection() 三个重载方法：
```java
getConnection(String url)

getConnection(String url, Properties prop)

getConnection(String url, String user, String password)
```
数据库的 URL 是指向数据库地址。下表列出了下来流行的 JDBC 驱动程序名和数据库的 URL。
|  RDBMS   | JDBC驱动程序的名称  | URL  |
|  ----  | ----  | ----  |
| Mysql  | com.mysql.jdbc.Driver | jdbc:mysql://hostname/ databaseName |
| Oracle  | oracle.jdbc.driver.OracleDriver | jdbc:oracle:thin:@hostname:port Number:databaseName |
| DB2  | COM.ibm.db2.jdbc.net.DB2Driver | jdbc:db2:hostname:port Number/databaseName |
| Sybase  | com.sybase.jdbc.SybDriver | jdbc:sybase:Tds:hostname: port Number/databaseName |  

3.4 创建连接对象    
下面三种形式 DriverManager.getConnection() 方法来创建一个连接对象，以 Mysql 为例。getConnection() 最常用形式要求传递一个数据库 URL，用户名 username 和密码 password。

1、使用数据库 URL 的用户名和密码
```java
String URL = "jdbc:mysql://localhost/EXAMPLE";
String USER = "username";
String PASS = "password"
Connection conn = DriverManager.getConnection(URL, USER, PASS);
```
2、只使用一个数据库 URL

然而，在这种情况下，数据库的 URL，包括用户名和密码。
```java
String URL = "jdbc:mysql://localhost/EXAMPLE?user=root&password=0909";
//Mysql URL 的参数设置详细可以查阅相关资料
Connection conn = DriverManager.getConnection(URL);
```
3、使用数据库的 URL 和一个 Properties 对象
```java
import java.util.*;

String URL = "jdbc:mysql://localhost/EXAMPLE";
Properties pro = new Properties( );

//Properties 对象，保存一组关键字-值对
pro.put( "user", "root" );
pro.put( "password", "" );

Connection conn = DriverManager.getConnection(URL, pro);
```
3.4 关闭JDBC连接
```java
conn.close();
```
