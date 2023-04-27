# qingge-Market
**（界面展示在后面）**
基于springboot+vue的电商平台。本人本科毕业设计作品。

安装说明：1.先安装npm，具体百度一下教程。使用npm运行前端vue项目  2.安装mysql，建一个库叫qingge，运行sql文件导入表。 3.安装redis 4.安装maven、idea等

##后端亮点：

1.使用jwt进行身份验证

 首先在登陆时，后端为该用户生成一个token，并发送给用户。用户将信息保存到LocalStorage中，并在以后将token放入请求头中
 
 后端通过拦截器实现：后端收到请求后，会先取出请求头中的token，并根据token查询用户信息，然后把用户信息放入ThreadLocal中,方便之后使用用户信息
 
 ![image](https://user-images.githubusercontent.com/78432919/221390051-ca585c04-8f8e-406e-b375-91b0d1a20962.png)
 
2.统一的异常处理

  自定义了异常类，并通过@ControllerAdvice注解实现了全局异常处理，使用@ExceptionHandler捕捉自定义异常
  
3.统一的返回结果

  自定义返回结果类Result
  ![image](https://user-images.githubusercontent.com/78432919/221390362-07a94cb1-66bc-4e23-aa19-132f406a6b17.png)
  
4.Redis数据库缓存热点数据

  后端配置了RedisTemplate操作redis数据库，将一些热点数据，如用户信息、商品信息存入redis，以此提高查询速度。
  
  后端会先去redis里查询信息，若有则返回，若没有则去mysql中查，查到后存入redis，然后返回。
  
  ![image](https://user-images.githubusercontent.com/78432919/221390504-1048eee4-43a0-4b69-80af-e20d6154e57b.png)
  
5.权限管理

  1.首先通过token拦截器，在一些controller上验证请求中是否携带token以及token是否合法
  
  2.自定义一个注解@Authority，注解可以加在类上和方法上，限定该类或者方法的请求权限。
  
  3.定义一个拦截器，判断映射方法是否加有该注解。若有注解，则判断用户是否有对应权限。
  
![image](https://user-images.githubusercontent.com/78432919/221390798-f2de7749-1e95-4be2-aecd-24e4ee3c2b71.png)
    
![image](https://user-images.githubusercontent.com/78432919/221390806-0a0755b2-fe24-4e67-816d-88fe52151d42.png)
    
6.防止上传相同文件

  用户上传文件，会先判断该文件的md5是否在数据库中已存在，若存在则不接收文件，直接使用已存在文件的url。

##界面展示：
1.首页
![image](https://user-images.githubusercontent.com/78432919/221390920-fe30f39a-b4c6-4c55-827d-589f47e357ba.png)

2.个人信息
![image](https://user-images.githubusercontent.com/78432919/231815487-8e349f99-ea3e-46db-80aa-b4ee62197397.png)

3.商品详情
![image](https://user-images.githubusercontent.com/78432919/221390949-7a792461-a00e-48ef-a7e9-4abeab4c1e72.png)

4.购买商品
![image](https://user-images.githubusercontent.com/78432919/231815664-c171bc07-7265-44b7-b5e7-a02f6d96b7e1.png)

5.购物车
![image](https://user-images.githubusercontent.com/78432919/231815831-4d462f02-0750-43c0-91a7-1734c3bb34f2.png)

6.订单
![image](https://user-images.githubusercontent.com/78432919/221391001-72759f7d-eb46-41f1-a349-f10d6247bebc.png)

7.后台管理

![image](https://user-images.githubusercontent.com/78432919/221391018-9f03293c-3b89-406a-8ac1-a0d8f3981faf.png)

8.轮播图管理
![image](https://user-images.githubusercontent.com/78432919/221391030-95052f52-943f-4feb-a17a-98ea6ce07264.png)

9.商品管理
![image](https://user-images.githubusercontent.com/78432919/221391038-9d4d9999-8f8e-4ffe-aec9-d9e2e129c206.png)
![image](https://user-images.githubusercontent.com/78432919/221391042-a29ad171-22ea-4174-9214-923056f9591b.png)
![image](https://user-images.githubusercontent.com/78432919/221391049-b459210a-60b8-4d9d-b596-b4a06a62e5d7.png)

10.订单管理


11.收入分析
![image](https://user-images.githubusercontent.com/78432919/221391070-be53a2a6-5aab-4231-b6a8-6c8169e2a4fc.png)
![image](https://user-images.githubusercontent.com/78432919/221391077-6258d5f9-efad-4a95-91f9-285ae0b4734a.png)
![image](https://user-images.githubusercontent.com/78432919/221391082-dcea278d-9389-428c-a319-92c2f7bcacf7.png)





