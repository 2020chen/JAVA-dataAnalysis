## 目录
- [JDBC简介](#JDBC简介)
- [JDBC SQL 语法](#JDBC SQL 语法)
- [JDBC 环境设置](#JDBC 环境设置)
- [创建 JDBC 应用程序](#创建 JDBC 应用程序)
- [JDBC基础](#JDBC基础)
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
``sql
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
连接数据库（实验楼中密码环境为空）：
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
2. 打开连接：需要使用 'DriverManager.getConnection() '方法创建一个`Connection`对象，它代表与数据库的物理连接

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

