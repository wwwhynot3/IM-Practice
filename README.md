# IM-Practice

IM-Practice is a simple IM project, which is used to practice the basic knowledge of IM.
nobody's IM practice project
------------------------------------------------------------------

## Current Project Structure

**Trying to use sse to push messages**

目前使用短轮询实现实时聊天，考虑使用SSE

用户消息和群消息按照用户和群的ID，每个会话按照两个用户或者群的ID生成表名，存储至MongoDB
RocketMQ消费方法有两种:

- (采用)RocketMQ中分为两个Topic，分别存储UserMessage和GroupMessage
- (未采用)RocketMQ中分为一个Topic，存储所有消息，一个Topic有两个Consumer Group，分别消费UserMessage和GroupMessage

在Consumer中，onMessage()函数消费实体类型，设置实体的ID为null，由MongoTemplate插入MongoDB中，MongoDB会自动生成由(
秒数+机器Id+进程Id+序列数组成的)ObjectId，保证消息的唯一性(并且依据网上测试，MongoDB不指定Id的插入速度最快)

RocketMQ发送消息时，使用sendAndReceive()方法，发送消息并且等待返回结果，返回由MongoDB生成的ObjectId

------------------------------------------------------------------

## Primary Project Structure

技术栈：springboot, netty，mongodb, mysql, rocketmq, rabbitmq, springcloud, redis

- mongodb:存储消息
- mysql:存储用户信息和群信息
- rocketmq：解耦消息推送和拉取，离线消息解耦
- rabbitmq：解耦红包消息
- redis：用户消息，群消息，群用户对应关系

模块划分

- manageer (用户注册，好友操作，群操作等一系列API操作)
- IM （与APP连接，用于接受和发送消息）
- Data Transfer (接受IM推送到RocketMQ中用户发送的消息)
- Offline (离线服务，目的对象不在线时，需要推送离线消息告知好友)

水平扩张的

数据流架构 ： 推拉模式

- 推模式： 用户发送消息

  APP ----> IM ---> rocketMQ ---> Data Transfer 消费 ---> 数据全部落入mongodb数据区

  ​                                                    (发现改消息所属用户离线) ---> 离线RocketMQ ---> Offline 消费 --->
  手机SDK ---> 推送用户通知

  ​                                                    (发送群消息时，destId对应群用户集合)

- 拉模式：用户接受消息

  APP 定时轮询 ---> IM ----> mongodb 查询消息

  问题：

    1. 推模式下，发送消息时，为什么不直接推送?

       在发送群消息时， qps不可控，虽然保证即时性，但是可能导致消息风暴和总线风暴，系统不稳定

    2. 定时轮询， 规定每次轮询只能拉取40条消息，拉取消息的qps由用户在线数决定，系统更可控，并且在实践下，即时性并不比推模式差很多

- 
