# 软件体系结构文档

## 文档修改历史

| 修改人员 | 日期     | 修改记录     | 版本号 |
| :------: | -------- | ------------ | ------ |
|   陈凌   | 2023/6/1 | 第一阶段完成 | 1.0    |

## 目录

1. 引言
   1. 编制目的
   2. 词汇表
   3. 参考资料
2. 产品概述
3. 逻辑视图
4. 组合视图
   1. 开发包图
   2. 运行时进程
   3. 物理部署
5. 架构设计
   1. 模块职责
   2. 用户界面层分解
      1. 职责
      2. 接口规范
   3. 业务逻辑层分解
      1. 职责
      2. 接口规范
   4. 数据层分解
      1. 职责
      2. 接口规范
   5. 信息视角

## 引言

### 编制目的

本报告详细完成对l2306互联网购票系统的概要设计，达到指导详细设计和开发的目的，同时实现和测试人员及用户的沟通。<br>
本报告面向开发人员、测试人员及最终用户而编写，是了解系统的导航。

### 参考资料

(1) IEEE标准

(2)《软件工程与计算（卷二）软件开发的技术基础》

## 产品概述

参考l2306用例文档和互联网购票系统软件需求规格说明中对产品的概括描述。

## 逻辑视图

- 处理静态设计模型

  l2306系统中，选择了分层体系结构风格，将系统分为4部分（view、controller、service、data）能够很好地示意整个高层抽象。view部分包含GUI页面的实现，controller部分负责接受前端发送的请求并分发给相应的service，service部分负责业务逻辑的实现，data部分负责数据的持久化和访问。分层体系结构的逻辑视角和逻辑设计方案如下图所示。

  <img src="https://seec-pkun.oss-cn-beijing.aliyuncs.com/image/%E9%80%BB%E8%BE%91%E8%A7%86%E8%A7%92.png" alt="逻辑视图分层图" />

  图1 参照体系结构风格的包图表达逻辑视角

## 组合视图

### 开发包图

- 表示软件组件在开发时环境中的静态组织
  - 描述开发包以及相互间的依赖
  - 绘制开发包图
    - 开发包图类似上述示意图画法

|      开发包       | 依赖的其他开发包                         |
| :---------------: | ---------------------------------------- |
|      mainui       | userui、vo                               |
|  routecontroller  | routeservice, po, vo                     |
|   routeservice    | routemapper, po                          |
|    routemapper    | Mybatis, po                              |
|      trainui      | ant-design-vue, vo, trainController      |
|  traincontroller  | trainservice, po, vo                     |
|   trainservice    | trainmapper, po, vo                      |
|    trainmapper    | Mybatis, po, vo                          |
|     stationui     | ant-design-vue, vo,  stationcontroller   |
| stationcontroller | stationservice, vo, util                 |
|  stationservice   | stationmapper, userservice, vo, po, util |
|   stationmapper   | Mybatis, po                              |
|      userui       | ant-design-vue, vo                       |
|  usercontroller   | userserivce, vo, po                      |
|    userservice    | usermapper, vo, po                       |
|    usermapper     | Mybatis, po                              |
|        vo         |                                          |
|        po         |                                          |
|       util        |                                          |
|  ant-design-vue   |                                          |
|      Mybatis      | JDBC                                     |
|     REST API      |                                          |

### 运行时进程

- 表示软件在运行时进程间的交互，描述系统的动态结构

  - 绘制进程图
- 示意图：
  - ​	![](https://seec-pkun.oss-cn-beijing.aliyuncs.com/image/%E8%BF%90%E8%A1%8C%E6%97%B6%E8%BF%9B%E7%A8%8B.png)


### 物理部署

- 处理如何将软件组件映射到硬件基础设施
- 示意图：
  

## 架构设计

- 描述功能分解和如何在不同的层中安排软件模块
  - 描述架构中的对象，包含架构图
  - 描述组件接口信息
    - 包括：语法、前置条件、后置条件

### 模块职责

- 模块视图

  ![服务器端模块视图](https://seec-pkun.oss-cn-beijing.aliyuncs.com/image/%E6%9C%8D%E5%8A%A1%E5%99%A8%E7%AB%AF%E6%A8%A1%E5%9D%97.png)

  服务端模块视图

  ![客户端模块视图](https://se2.oss-cn-beijing.aliyuncs.com/%E6%9C%AA%E5%91%BD%E5%90%8D%E6%96%87%E4%BB%B6.png)

- 各层职责

  - 客户端各层的职责

    |     层     |                        职责                        |
    | :--------: | :------------------------------------------------: |
    |  启动模块  |         负责初始化网络通信，渲染浏览器界面         |
    | 用户界面层 |      基于浏览器窗口的l2306系统客户端用户界面       |
    | 业务逻辑层 | 对于用户界面的输入和选择进行响应并进行业务处理逻辑 |
    |  网络模块  |             利用REST API和服务器端交互             |
    
  - 服务端各层的职责

    |        层        |                             职责                             |
    | :--------------: | :----------------------------------------------------------: |
    |     启动模块     |               负责初始化通络通信机制，加载Bean               |
    | 服务器端网络模块 |          利用Tomcat和Java注解与反射开启服务器端接口          |
    |     拦截器层     | 自定义拦截器，借助jwt进行用户身份校验，拦截除登录注册外的api |
    |   Controller层   | 负责响应客户端发送的请求并且转发给Service层并且返回请求结果  |
    |    Service层     |                负责和数据层交互，实现业务逻辑                |
    |      data层      |                负责数据的持久化及数据访问接口                |

  

- 层之间调用接口

|                                     接口                                      |     服务调用方     |     服务提供方     |
| :---------------------------------------------------------------------------: | :----------------: | :----------------: |
|       RouteService<br>StationService<br>UserService<br>trainService<br>       | 服务端Controller层 |  服务端Service层   |
|         RouteMapper<br>StationMapper<br>UserMapper<br>trainMapper<br>         |  服务端Service层   |    服务端data层    |
| RouteController<br>StationController<br>UserController<br>TrainController<br> |    客户端展示层    | 服务端Controller层 |



### 用户界面层分解

#### 职责

- 类图
- 接口规范
- 需要的服务接口

#### 用户界面层模块接口规范

manageUser模块的接口规范

| 接口名     | 语法        | **前置条件**   | 后置条件                                      |
| ---------- | :---------- | -------------- | --------------------------------------------- |
| addManager | addManage() | 拥有管理员身份 | 将管理者的信息交给后端AdminService.addManager |

| 需要的服务（需接口）   | 服务                               |
| ---------------------- | ---------------------------------- |
| AdminMapper.addManager | 将获得的管理员数据持久化到数据库中 |

TrainList模块的接口规范

| 接口名      | 语法          | **前置条件** | 后置条件           |
| ----------- | :------------ | ------------ | ------------------ |
| pageChane   | pageChange()  |              | 更新显示的车次列表 |
| goToDetails | goToDetails() |              | 展示车次详情       |

| 需要的服务（需接口）              | 服务             |
| --------------------------------- | ---------------- |
| TrainService.retrieveTrains       | 获取车次列表     |
| TrainService.retrieveTrainDetails | 获取指定车次详情 |

header模块的接口规范

| 接口名       | 语法           | **前置条件** | 后置条件           |
| ------------ | :------------- | ------------ | ------------------ |
| selectMenu   | selectMenu()   |              | 更新显示的车次列表 |
| goToHome     | goToHome()     |              | 回到主页           |
| goToUserInfo | goToUserInfo() |              | 前往用户信息页面   |

| 需要的服务（需接口）       | 服务         |
| -------------------------- | ------------ |
| AccountService.getUserInfo | 获取个人信息 |

manageTrain模块的接口规范

| 接口名      | 语法          | **前置条件**     | 后置条件                                         |
| ----------- | :------------ | ---------------- | ------------------------------------------------ |
| addTrain    | addTrain()    | 管理员身份，下同 | 将车次信息提交给后端，显示回调结果，更新车次列表 |
| addTrain    | addTrain()    |                  | 将车次信息提交给后端，显示回调结果，更新车次列表 |
| showCoupon  | showCoupon()  |                  | 返回车次的全部优惠策略                           |
| deleteTrain | deleteTrain() |                  | 将删除的车次id发送给后端，更新车次列表           |
| deleteOrder | deleteOrder() |                  | 将删除的订单id发送给后端，更新订单列表           |

| 需要的服务（需接口）            | 服务                         |
| ------------------------------- | ---------------------------- |
| TrainService.addTrain           | 添加车次                     |
| TrainService.insertTrainInfo    | 添加车次信息                 |
| CouponService.getTrainAllcoupon | 获得指定车次下的所有优惠策略 |
| TrainService.updateTrainInfo    | 更新车次信息                 |

info模块的接口规范

| 接口名             | 语法                 | **前置条件**           | 后置条件                                         |
| ------------------ | :------------------- | ---------------------- | ------------------------------------------------ |
| saveModify         | saveModify()         | 修改的信息符合格式要求 | 持久化到数据库                                   |
| modifylnfo         | modifyInfo()         |                        | 将车次信息提交给后端，显示回调结果，更新车次列表 |
| cancelModify       | cancelModify()       |                        | 将modify设置为假                                 |
| confirmCancelOrder | confirmCancelOrder() |                        | 撤销订单                                         |

| 需要的服务（需接口）          | 服务         |
| ----------------------------- | ------------ |
| AccountService.updateUserInfo | 更新个人信息 |
| OrderService.annulOrder       | 撤销订单     |

### 业务逻辑层分解

业务逻辑层包括多个针对界面的业务逻辑处理对象。例如，  AdminService对象负责处理网站管理界面的业务逻辑；AccountService对象负责处理用户界面相关的业务逻辑；TrainService对象负责处理车次界面相关的业务逻辑；TrainService对象负责处理车次界面相关的业务逻辑；OrderService对象负责处理订单界面相关的业务逻辑；CouponService对象负责处理优惠卷相关的业务逻辑。

#### 业务逻辑层模块的职责

每个bl模块由Service接口与它的ServiceImpl实现组成

| 模块     | 职责                             |
| -------- | :------------------------------- |
| adminbl  | 负责实现网站管理界面所需要的服务 |
| userbl   | 负责实现用户界面所需要的服务     |
| trainbl  | 负责实现车站界面所需要的服务     |
| orderbl  | 负责实现订单界面所需要的服务     |
| couponbl | 负责实现优惠所需要的服务         |

#### 业务逻辑层模块的接口规范

adminbl, userbl, trainbl, orderbl, couponbl模块的接口规范分别如下

adminbl模块的接口规范  

| 提供的服务（供接口）               | 语法                                                                     | 前置条件                 | 后置条件                                    |
| ---------------------------------- | ------------------------------------------------------------------------ | ------------------------ | ------------------------------------------- |
| AdminService.addManager            | public ResponseVO addManager(UserForm userForm);                         | 待注册车站管理员信息合法 | 生成相应的持久化对象，并添加到数据库中      |
| AdminService.getAllManagers        | public List< User> getAllManagers();                                     | 无                       | 返回所有车站管理员信息的持久化对象          |
| AdminService.getTrainManager       | public User getTrainManager(int TrainId)                                 | 无                       | 返回某个车站管理员信息的持久化对象          |
| AdminService.getUserByNameOrEmail  | public List<User> getUserByNameOrEmail(Integer type, String information) | 无                       | 返回对应车站管理员信息的持久化对象          |
| AdminService.updateUserInformation | public int updateUserInformation(User user)                              | 无                       | 更新相应的User对象数据                      |
| AdminService.delUser               | public ResponseVO delUser(Integer userId)                                | 无                       | 删除持久化对象                              |
| AdminService.resetPassword         | public ResponseVO resetPassword(Integer userId)                          | 无                       | 更新相应的User对象数据                      |
| AdminService.getAllUsers           | public List<User> getAllUsers()                                          | 无                       | 返回所有User的持久化对象                    |
| AdminService.updateUserCredit      | public ResponseVO updateUserCredit(Integer id,Integer creditNum)         | 无                       | 更新相应的User对象数据,creditChange对象数据 |

| 需要的服务（需接口）                                       | 服务                     |
| ---------------------------------------------------------- | ------------------------ |
| @Autowired AdminMapper                                     | 得到User数据库接口的引用 |
| AdminMapper.addManager(User user)                          | 插入单一持久化对象       |
| AdminMapper.getAllManagers()                               | 查找多个持久化对象       |
| AdminMapper.getAccountById(int userId)                     | 查找单一持久化对象       |
| AdminMapper.getTrainManager(int TrainId)                   | 查找单一持久化对象       |
| AdminMapper.retrieveUserByName(String userName)            | 查找单一持久化对象       |
| AdminMapper.retrieveUserByEmail(String email)              | 查找单一持久化对象       |
| AdminMapper.updateUserInfo(User user)                      | 更新持久化对象           |
| AdminMapper.delAccountById(Integer userId)                 | 删除持久化对象           |
| AdminMapper.selectAllUsers()                               | 查找多个持久化对象       |
| AdminMapper.updateUserCredit(Integer id,Integer creditNum) | 更新持久化对象           |

userbl模块的接口规范  

| 提供的服务（供接口）            | 语法                                                                                   | 前置条件                                   | 后置条件                                                       |
| ------------------------------- | -------------------------------------------------------------------------------------- | ------------------------------------------ | -------------------------------------------------------------- |
| AccountService.registerAccount  | ResponseVO registerAccount(UserVO userVO);                                             | userVO符合输入规则                         | 持久化相应的User对象                                           |
| AccountService.login            | User login(UserForm userForm);                                                         | userForm符合输入规则                       | 查找是否存在相应的User，根据输入的UserForm返回登录验证的结果   |
| AccountService.getUserInfo      | User getUserInfo(int id);                                                              | 已登录                                     | 根据id查找是否存在相应的User，若存在则返回User对象             |
| AccountService.updateUserInfo   | ResponseVO updateUserInfo(int id, String password,String username,String phonenumber); | password,username, phonenumber符合输入规则 | 根据id, password, username, phonenumber 更新相应的User对象数据 |
| AccountService.registerSenior   | ResponseVO registerSenior(int id,int type,String message);                             | id对应的用户未注册会员，type为 1 或 2      | 根据id, type, message注册会员，持久化相应对象                  |
| AccountService.updateUserCredit | int updateUserCredit(CreditChange creditChange)                                        |                                            | 持久化相应CreditChange对象                                     |
| AccountService.creditChange     | ResponseVO creditChange(int id)                                                        |                                            | 返回持久化对象列表                                             |
| AccountService.addCredit        | ResponseVO addCredit(int id, double credit);                                           | id为对应的用户id，credit为变化后信用值     | 根据id，credit更新相应的User对象数据                           |

| 需要的服务（需接口）                                                                    | 服务                         |
| --------------------------------------------------------------------------------------- | ---------------------------- |
| @Autowired AccountMapper                                                                | 得到User数据库接口的引用     |
| AccountMapper.createNewAccount(User user)                                               | 在数据库中插入User对象       |
| AccountMapper.getAccountByName(String email)                                            | 根据email查找单一持久化对象  |
| AccountMapper.getAccountById(int id)                                                    | 根据id查找单一持久化对象     |
| AccountMapper.updateAccount(int id,String password,String username, String phonenumber) | 更新单一持久化对象           |
| AccountMapper.getVipById(int id)                                                        | 根据id查找单一持久化对象     |
| AccountMapper.createNewVip(Vip vip)                                                     | 在数据库中插入Vip对象        |
| AccountMapper.updateUserType(int userId, UserType userType);                            | 更新单一持久化对象           |
| AccountMapper.updateUserCredit(int id,int credit)                                       | 更新单一持久化对象           |
| AccountMapper.addCreditChange(CreditChange creditChange)                                | 插入单一持久化对象           |
| AccountMapper.getAllUserCreditChange(Integer userId)                                    | 根据userId查找多个持久化对象 |

trainbl模块的接口规范  

| 提供的服务（供接口）                         | 语法                                                                            | 前置条件             | 后置条件                                                |
| -------------------------------------------- | ------------------------------------------------------------------------------- | -------------------- | ------------------------------------------------------- |
| TrainService.addTrain                        | void addTrain(TrainVO TrainVO);                                                 | 已登录车次管理员账号 | 持久化相应的Train对象                                   |
| TrainService.updateTrainInfo                 | void updateTrainInfo(Integer TrainId, String TrainType,Integer Trains);         | 已登录车次管理员账号 | 持久化更新涉及的Train对象的数据                         |
| TrainService.retrieveTrains                  | List< TrainVO> retrieveTrains();                                                | 已登录               | 返回所有TrainVO对象的列表                               |
| TrainService.retrieveTrainDetails            | TrainVO retrieveTrainDetails(Integer TrainId);                                  |                      | 返回一个TrainVO对象                                     |
| TrainService.getTrainCurNum                  | int getTrainCurNum(Integer TrainId,String TrainType);                           | 已存在对应对象       | 根据TrainId和TrainType返回剩余车次数                    |
| TrainService.updateTrainInfo                 | void updateTrainInfo(Integer TrainId, String TrainType, Integer Trains);        | 已登录车次管理员账号 | 持久化更新涉及的Train对象的数据                         |
| TrainService.getTrainCurNum                  | int getTrainCurNum(Integer TrainId, String TrainType);                          | 已存在对应对象       | 根据TrainId和TrainType返回剩余车次数                    |
| TrainService.retrieveTrainTrainInfo          | List< TrainTrain> retrieveTrainTrainInfo(Integer TrainId);                      |                      | 根据TrainId返回某个车次的全部车次信息                   |
| TrainService.insertTrainInfo                 | void insertTrainInfo(TrainTrain TrainTrain);                                    |                      | 持久化相应的TrainTrain对象                              |
| TrainService.delTrainInfo                    | ResponseVO delTrainInfo(Integer TrainId)                                        |                      | 删除相应的持久化对象                                    |
| TrainService.updateTrainNum                  | ResponseVO updateTrainNum(Integer TrainId, Integer newTrainNum)                 | 更新相应的持久化对象 |                                                         |
| TrainService.retrieveTrainsByBizAndAdd       | List< TrainVO> retrieveTrainsByBizAndAdd(String bizRegion, String address);     |                      | 根据bizRegion和address返回符合条件的TrainVO车次对象列表 |
| TrainService.retrieveAllBizRegions           | List< BizRegion> retrieveAllBizRegions();                                       |                      | 返回商圈列表                                            |
| TrainService.retrieveTrainsByTrainAndTrainVO | List< TrainVO> retrieveTrainsByTrainAndTrainVO(TrainAndTrainVO TrainAndTrainVO) |                      | 返回符合条件的TrainVO车次对象列表                       |
| TrainService.getTrainByManager               | TrainVO getTrainByManager(Integer TrainManagerId);                              |                      | 根据车次管理员的Id找到他管理的车次                      |
| TrainService.deleteTrain                     | int deleteTrain(Integer TrainId);                                               |                      | 删除车次                                                |
| TrainService.updateTrainInfo                 | ResponseVO TrainService.updateTrainInfo(TrainVO TrainVO)                        | 车次管理员账号       | 更新车次信息                                            |

| 需要的服务（需接口）                                                         | 服务                         |
| ---------------------------------------------------------------------------- | ---------------------------- |
| @Autowired TrainMapper                                                       | 得到Train数据库接口的引用    |
| @Autowired AccountService                                                    | 得到User服务对象的引用       |
| AccountService.getUserInfo(int id)                                           | 获取对应的User对象           |
| TrainMapper.insertTrain(Train Train)                                         | 插入单一持久化对象           |
| TrainMapper.selectAllTrain()                                                 | 查找多个持久化对象           |
| TrainMapper.selectById(Integer id)                                           | 根据id进行查找单一持久化对象 |
| @Autowired TrainMapper                                                       | 得到Train数据库接口的引用    |
| TrainMapper.selectTrainsByTrainId(Integer TrainId)                           | 查找多个持久化对象           |
| TrainMapper.insertTrain(TrainTrain TrainTrain)                               | 插入单一持久化对象           |
| TrainMapper.updateTrainInfo(Integer TrainId,String TrainType,Integer curNum) | 更新单一持久化对象           |
| TrainMapper.getTrainCurNum(Integer TrainId,String TrainType)                 | 查找持久化对象数量           |
| TrainMapper.selectTrainByBizAndAdd(BizRegion bizRegion, String address)      | 查找多个持久化对象           |
| TrainMapper.retrieveTrainsByTrainAndTrainVO(TrainAndTrainVO TrainAndTrainVO) | 查找多个持久化对象           |
| TrainMapper.delTrainByTrainId(Integer TrainId)                               | 删除单一持久化对象           |
| TrainMapper.selectTrainsByTrainId(Integer TrainId)                           | 查找单一持久化对象           |
| TrainMapper.updateTrainNum(Integer TrainNum, Integer TrainId)                | 更新单一持久化对象           |
| TrainMapper.updateTrainInfo(UpdateTrainVO updateTrainVO)                     | 更新单一持久化对象           |
| TrainMapper.deleteTrain(Integer TrainId);                                    | 删除单一持久化对象           |
| TrainMapper.selectByTrainManagerId(Integer id);                              | 查找单一持久化对象           |

orderbl模块的接口规范  

| 提供的服务（供接口）                   | 语法                                                           | 前置条件                          | 后置条件                                                   |
| -------------------------------------- | -------------------------------------------------------------- | --------------------------------- | ---------------------------------------------------------- |
| OrderService.addOrder                  | ResponseVO addOrder(OrderVO orderVO);                          | orderVO符合输入规则               | 持久化相应的Order对象                                      |
| OrderService.getAllOrders              | List< Order> getAllOrders();                                   |                                   | 返回所有Order对象                                          |
| OrderService.getUserOrders             | List< Order> getUserOrders(int userid);                        | 已登录                            | 根据userid返回所有Order对象                                |
| OrderService.annulOrder                | ResponseVO annulOrder(int orderid);                            | 已存在对应Order对象               | 根据orderid持久化更新涉及的Order对象的数据，用户信用值更新 |
| OrderService.getTrainOrders            | List< Order> getTrainOrders(Integer TrainId);                  | 已登录                            | 根据TrainId返回所有Order对象                               |
| OrderService.executeOrder              | ResponseVO executeOrder(int orderId, int userId);              | orderId对应的订单必须是booked状态 | 订单状态变成已执行，用户信用值更新                         |
| OrderService.getBookedTrains           | ResponseVO getBookedTrains(int userId);                        | 已登录                            | 返回该用户所有住过的车次                                   |
| OrderService.getAllUsersOrdersInATrain | ResponseVO getAllUsersOrdersInATrain(int userId, int TrainId); | 已登录                            | 返回该用户在某个车次的所有订单                             |
| OrderService.comment                   | ResponseVO comment(CommentVO commentVO)                        | 订单状态为已执行                  | 持久化相应的Comment对象                                    |
| OrderService.getTrainExceptionOrder    | List<Order> getTrainExceptionOrder(int TrainId)                |                                   | 根据TrainId返回异常Order对象                               |
| OrderService.executeExceptionOrder     | ResponseVO executeExceptionOrder(int orderId)                  |                                   | 根据orderId更新Order对象                                   |
| OrderService.checkOutOrder             | void checkOutOrder(int orderId)                                |                                   | 更新相应的Order对象                                        |
| OrderService.getOrderComment           | ResponseVO getOrderComment(int orderId)                        |                                   | 根据orderId返回Comment对象                                 |
| OrderService.getTrainComment           | ResponseVO getTrainComment(int TrainId)                        |                                   | 根据TrainId返回Comment对象                                 |

| 需要的服务（需接口）                                                           | 服务                      |
| ------------------------------------------------------------------------------ | ------------------------- |
| @Autowired OrderMapper                                                         | 得到Order数据库接口的引用 |
| @Autowired AccountService                                                      | 得到User服务对象的引用    |
| @Autowired TrainService                                                        | 得到Train服务对象的引用   |
| TrainService.getTrainCurNum(Integer TrainId,String TrainType)                  | 查找持久化对象数量        |
| AccountService.getUserInfo(int id)                                             | 获取对应的User对象        |
| OrderMapper.addOrder(Order order)                                              | 插入单一持久化对象        |
| OrderMapper.getAllOrders()                                                     | 查找多个持久化对象        |
| OrderMapper.getUserOrders(int userid)                                          | 查找多个持久化对象        |
| OrderMapper.annulOrder(int orderid)                                            | 更新单一持久化对象        |
| OrderMapper.getOrderById(int orderid)                                          | 查找单一持久化对象        |
| TrainService.updateTrainInfo(Integer TrainId, String TrainType,Integer Trains) | 更新持久化对象的数据      |
| AccountService.updateUserCredit(Integer userId, Double creditToMinus)          | 更新持久化对象的数据      |
| OrderMapper.exceptionOrder(int orderid)                                        | 更新持久化对象的数据      |
| OrderMapper.executeOrder(int orderId, String checkInDate)                      | 更新持久化对象的数据      |
| OrderMapper.dealOutCheckoutDate(int orderId)                                   | 更新持久化对象的数据      |
| OrderMapper.checkOutOrder(int orderId, String checkOutDate)                    | 更新持久化对象的数据      |
| CommentService.addComment(CommentVO commentVO)                                 | 更新持久化对象的数据      |
| CommentService.getComment(orderId)                                             | 查找单个持久化对象        |
| CommentService.getAllTrainComment(TrainId)                                     | 查找多个持久化对象        |
| accountService.getUserInfo(orderVO.getUserId())                                | 查找单个持久化对象        |

couponbl模块的接口规范  

| 提供的服务（供接口）                    | 语法                                                           | 前置条件 | 后置条件                                |
| --------------------------------------- | -------------------------------------------------------------- | -------- | --------------------------------------- |
| CouponService.getMatchOrderCoupon       | List< Coupon> getMatchOrderCoupon(OrderVO orderVO);            |          | 根据orderVO返回所有符合条件的Coupon对象 |
| CouponService.getTrainAllCoupon         | List< Coupon> getTrainAllCoupon(Integer TrainId);              |          | 根据TrainId返回所有Coupon对象           |
| CouponService.addTrainTargetMoneyCoupon | void addTrainTargetMoneyCoupon(TrainTargetMoneyCoupon coupon); |          | 持久化相应的Coupon对象                  |

| 需要的服务（需接口）                          | 服务                       |
| --------------------------------------------- | -------------------------- |
| @Autowired CouponMapper                       | 得到Coupon数据库接口的引用 |
| CouponMapper.insertCoupon(Coupon coupon)      | 插入单一持久化对象         |
| CouponMapper.selectByTrainId(Integer TrainId) | 查找多个持久化对象         |

### 数据层分解

数据层主要给业务逻辑层提供数据访问服务，包括对于持久化数据的增删改查，持久化数据的保存方式是采用ProgreSQL数据库；对于热点数据的缓存，保存形式是采用redis数据库。

#### 数据层的职责

| 模块          | 职责                                                    |
| ------------- | ------------------------------------------------------- |
| UserMapper    | 基于PostgreSQL数据库的account表的接口，提供增删改查服务 |
| StationMapper | 基于PostgreSQL数据库的hotel表的接口，提供增删改查服务   |
| RouteMapper   | 基于PostgreSQL数据库的room表的接口，提供增删改查服务    |
| TrainMapper   | 基于PostgreSQL数据库的order表的接口，提供增删改查服务   |


#### 数据层的接口规范

| 提供的服务（供接口）                 | 语法                                                                            | 前置条件                      | 后置条件                               |
| ------------------------------------ | ------------------------------------------------------------------------------- | ----------------------------- | -------------------------------------- |
| AdminMapper.addManager               | int addManager(User user);                                                      | 同样email的user在表中不存在   | 在数据库中增加一个po记录               |
| AdminMapper.getAllManagers           | List< User> getAllManagers();                                                   | 无                            | 返回所有User                           |
| AdminMapper.getAccountById           | AdminMapper.getAccountById(int userId)                                          | 无                            | 查找单一持久化对象                     |
| AdminMapper.getHotelManager          | AdminMapper.getHotelManager(int hotelId)                                        | 无                            | 查找单一持久化对象                     |
| AdminMapper.retrieveUserByName       | AdminMapper.retrieveUserByName(String userName)                                 | 无                            | 查找单一持久化对象                     |
| AdminMapper.retrieveUserByEmail      | AdminMapper.retrieveUserByEmail(String email)                                   | 无                            | 查找单一持久化对象                     |
| AdminMapper.updateUserInfo           | AdminMapper.updateUserInfo(User user)                                           | 无                            | 更新持久化对象                         |
| AdminMapper.delAccountById           | AdminMapper.delAccountById(Integer userId)                                      | 无                            | 删除持久化对象                         |
| AdminMapper.selectAllUsers           | AdminMapper.selectAllUsers()                                                    | 无                            | 查找多个持久化对象                     |
| AdminMapper.updateUserCredit         | AdminMapper.updateUserCredit(Integer id,Integer creditNum)                      | 无                            | 更新持久化对象                         |
| AccountMapper.createNewAccount       | int createNewAccount(User user);                                                | 同样email的user在表中不存在   | 在数据库中增加一个po记录               |
| AccountMapper.getAccountByName       | User getAccountByName(String email);                                            | 无                            | 按email进行查找返回相应的User结果      |
| AccountMapper.getAccountById         | User getAccountById(int id);                                                    | 无                            | 按id进行查找返回相应的User结果         |
| AccountMapper.updateAccount          | int updateAccount(int id,String password, String username, String phonenumber); | 数据库中存在相同id的po        | 更新一个po                             |
| AccountMapper.updateUserCredit       | User updateUserCredit(int id,int credit)                                        | 数据库中存在相同id的po        | 更新一个po                             |
| AccountMapper.updateUserType         | int updateUserType(int userId, UserType userType);                              | 无                            | 更新单一持久化对象                     |
| AccountMapper.addCreditChange        | int addCreditChange(CreditChange creditChange)                                  | 无                            | 插入单一持久化对象                     |
| AccountMapper.getAllUserCreditChange | List<CreditChange> getAllUserCreditChange(Integer userId)                       | 无                            | 根据userId查找多个持久化对象           |
| TrainMapper.selectAllTrain           | List< Train> selectAllTrain();                                                  | 无                            | 返回所有Train                          |
| TrainMapper.selectById               | Train selectById(Integer id);                                                   | 无                            | 按id进行查找返回相应的Train结果        |
| TrainMapper.selectTrainByBizAndAdd   | List<TrainVO> selectTrainByBizAndAdd(BizRegion bizRegion);                      | 无                            | 返回相应的Train结果                    |
| TrainMapper.updateTrainInfo          | int updateTrainInfo(UpdateTrainVO updateTrainVO);                               | 无                            | 更新相应的Train                        |
| TrainMapper.deleteTrain              | int deleteTrain(Integer TrainId)                                                | 无                            | 删除相应的Train                        |
| UserMapper.clearManager              | void clearManager(Integer userId)                                               | 无                            | 删除相应的Manager                      |
| RouteMapper.updateRouteInfo          | int updateRouteInfo(Integer routeId,String routeType,Integer curNum);           | 数据库中存在相同RouteId的po   | 更新一个po                             |
| RouteMapper.insertRoute              | int insertRoute(Route route);                                                   | 同样的route在表中不存在       | 在数据库中增加一个po记录               |
| RouteMapper.selectRouteByHotelId     | List< Route> selectRouteById(Integer RouteId);                                  | 无                            | 按RouteId进行查找返回相应的Route结果   |
| RouteMapper.delRouteByRouteId        | void delRouteInfo(Integer routeId)                                              | 无                            | 删除单一持久化对象                     |
| RouteMapper.selectRouteByRouteId     | Route selectRouteByRouteId(Integer routeId)                                     | 无                            | 查找单一持久化对象                     |
| RouteMapper.updateRouteNum           | int updateRouteNum(Integer routeNum, Integer routeId)                           | 无                            | 更新单一持久化对象                     |
| StationMapper.addStation             | int addStation(Station station);                                                | 同样的station在表中不存在     | 在数据库中增加一个po记录               |
| StationMapper.getAllStations         | List< Station> getAllStations();                                                | 无                            | 返回所有Station                        |
| StationMapper.annulStation           | int annulStation(int stationid);                                                | 数据库中存在相同stationid的po | 更新一个po                             |
| StationMapper.getStationById         | Order getStationById(int stationid);                                            | 无                            | 按stationid进行查找返回相应的Order结果 |
| StationMapper.exceptionStation       | int exceptionStation(int Stationid)                                             | 更新持久化对象的数据          |
| RedisUtil.hasKey                     | boolean hasKey(String key)                                                      | 无                            | 按key进行查找返回结果                  |
| RedisUtil.get                        | Object get(String key)                                                          | 无                            | 按key进行查找返回Object                |
| RedisUtil.set                        | boolean set(final String key, Object value))                                    | 无                            | 在数据库中增加一个key:value记录        |
| RedisUtil.delete                     | boolean delete(String key)                                                      | 无                            | 按key进行查找删除一条记录              |
| RedisUtil.expire                     | void expire(String key,long exTime)                                             | 无                            | 按key设置一条记录exTime秒后过期        |

### 信息视角

- 数据持久化对象（PO）

  - User类包含用户的编号（Integer，主键）、密码（String）、用户名（String）、电话号码（String）、信用分（double）、用户类型（枚举类型UserType）
  - Route类包含路线的编号（Integer，主键）、路线的名称（String）、所有车站的编号（List（Station））
  - Train类包含列车的编号（Integer，主键）、列车的名称（String）、列车起始站（Station）、列车终点站（Station）、日期（date）、所有出发时间（List（Time））、所有离开时间（List（Time））
  - Station类包含车站的编号（Integer、主键）、车站的名称（String）

    
- postgresql数据库表

  数据库中包括User表，Train表，Station表，Route表表中字段与PO类中属性相同，主键已在PO中说明，PO中的String在数据库中统一为varchar(255)，枚举类型统一为varchar(255)

