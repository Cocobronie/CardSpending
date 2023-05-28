# CardSpending
**校园卡消费系统:**

基于之前《RFIF与智能卡设计》课程课内实验构建的读卡器卡片实验系统（手机-13.56MHz- rfid卡片）进行拓展，实现读卡器系统与远程服务器（高层）的通信，并在高层构建数据库以及相应的管理系统，组成一个校园卡充值与消费系统，远程服务器须为任务组内另一个同学的手机或者是笔记本。要求完成整个物联网系统的方案设计（含软硬件框图，服务器端有数据库设计，人机界面管理设计）、程序设计、程序实现、程序测试与验证。

## 一、主要功能

### 1、用户刷卡登录

- 将卡片靠近读卡器
- 利用NFC功能读取IC卡中存储的用户ID以及password，再向远程服务器发出HTTP请求，远程服务器发送数据到手机中，最后页面响应变化反应给用户。


### 2、充值功能

- 输入充值金额或者点击相应的金额按钮使得确认按钮可点击
- 点击确认按钮，弹出**ReadDialog**,
- 将卡片贴近读卡器，**向卡片中写入信息**同时**更新服务器中的信息**
- 将更改的充值信息更新到UI界面上，可以看到下图**账户余额**变为250元
![image](https://github.com/Cocobronie/CardSpending/assets/98938169/2d064c03-5167-449e-bba0-0ad49e31ebe0)



### 3、消费功能

- 输入消费金额或者点击相应的金额按钮使得确认按钮可点击
- 点击确认按钮，弹出**ReadDialog**,
- 将卡片贴近读卡器，**向卡片中写入信息**同时**更新服务器中的信息**
- 将更改的充值信息更新到UI界面上，可以看到下图**账户余额**变为230元

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/4333d10c-d09a-4514-a556-a8db71b25496)


### 4、历史记录查看功能

- 进入“我的”界面，点击历史记录
- 跳转到历史记录界面
- 手机请求服务器数据，并更新到UI界面
- 可以看到下图出现最新的充值消费记录


## 二、实现方法

### 整体设计

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/be4bcd04-1898-4007-9d16-220e7576d008)

### 服务器端

- 使用**Node.js**实现后端逻辑
- 使用**Apipost**写接口文档
- 使用**花生壳**做****内网穿透****实现外网可以访问服务器

#### 1、用户登录接口

[用户登录 - Powered by Apipost V7](https://console-docs.apipost.cn/preview/bfb37dde95f7dea8/793a161f3c06bbe9?target_id=8533f09a-d158-47f9-8ce5-d747009c5293)

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/eb64cb31-5e94-414b-b2bc-add5b61400fd)



#### 2、余额更新接口

[更新余额 - Powered by Apipost V7](https://console-docs.apipost.cn/preview/a7e5b575ed757db3/49bcb1e57f19c36c?target_id=d24e4644-8478-4315-8d2b-d6800efe48a1)

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/eee4963a-2325-4712-9b5f-2f49b0923383)


#### 3、查询历史记录接口

[查询历史记录 - Powered by Apipost V7](https://console-docs.apipost.cn/preview/83deb98d1f9881c6/2f37ecca48c2afe8?target_id=ab9700ab-5f9f-4475-8bee-ef39f742f5a2)

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/29d51f11-3fc2-48bb-a16c-f3f495095340)


#### 4、添加历史记录接口

[添加历史记录接口 - Powered by Apipost V7](https://console-docs.apipost.cn/preview/3948990c824fc8cd/4f01552dd8b40f0f?target_id=d33893d9-192a-4a03-b327-4e0fbc777d97)

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/150171e3-beee-4667-a99d-faebb39b2425)


#### 5、Node数据库连接

这里是连接本地数据库



#### 6、数据库设计

一共有两个表格


**数据库：dbsqlconnect**

- **StuInfo**

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/0441746c-1ded-419b-86c1-d645d65780dd)

- **History**

![image](https://github.com/Cocobronie/CardSpending/assets/98938169/df9a54bd-bb05-405e-8822-c57fb6b88aaf)

#### 7、花生壳内网穿透

[花生壳管理 - 内网穿透 (oray.com)](https://console.hsk.oray.com/forward)

<img width="1014" alt="Untitled 17-1685252073223-23" src="https://github.com/Cocobronie/CardSpending/assets/98938169/107ff375-139b-4e5c-9646-f3ff59c31559">

这里需要注意的是：

**内网的ip地址有可能改变，所以需要在使用之前确认**。

### 客户端

客户端的设计主要分为ui设计，以及一些逻辑实现。其中逻辑部分包括：

- 读卡部分
- 数据库操作
- 网络连接
- 界面数据共享与更新

#### 1、读卡部分



#### 2、网络连接

- **用户刷卡登录请求服务器数据**

- **用户充值消费更改服务器数据**

- **用户获取历史记录数据**

#### 3、界面数据共享与更新
