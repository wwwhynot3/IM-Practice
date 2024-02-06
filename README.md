# IM-Practice
nobody's IM practice project

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

  ​													(发现改消息所属用户离线) ---> 离线RocketMQ ---> Offline 消费 ---> 手机SDK ---> 推送用户通知  

  ​													(发送群消息时，destId对应群用户集合)

- 拉模式：用户接受消息

  APP 定时轮询 ---> IM ----> mongodb 查询消息

  问题：

  1. 推模式下，发送消息时，为什么不直接推送?

     在发送群消息时， qps不可控，虽然保证即时性，但是可能导致消息风暴和总线风暴，系统不稳定

  2. 定时轮询， 规定每次轮询只能拉取40条消息，拉取消息的qps由用户在线数决定，系统更可控，并且在实践下，即时性并不比推模式差很多

- 
