# AntiRecall 反撤回插件

这是一个基于[`mirai`](https://github.com/mamoe/mirai) 机器人框架的插件

不想折腾模块? 在mirai上安装本插件进行简单配置即可实现

![image](images/show.png)

## 下载
[![image](https://img.shields.io/github/downloads/MX233/AntiRecall/total)](https://github.com/MX233/AntiRecall/releases)

# 一定要先看完README

## 本插件功能
- 获取被撤回的消息
- 闪照破解

## 前置插件
- [chat-command](https://github.com/project-mirai/chat-command)

## 指令
**前缀一般是 `/`**

| 指令                       | 作用           | 示例                                         |
|:-------------------------|:-------------|:-------------------------------------------|
| `ar` `en` `<true/false>` | `设置开启或关闭`    | ar en true                                 |
| `ar` `ca`  `<time>`      | `设置消息缓存时间`   | ar ca 30                                   |
| `ar` `fm`  `<text>`      | `设置转发消息的消息链` | ar fm ${sender}发送了一条${type}消息:\n${message} |
| `ar` `reload`            | `重载配置`       | -                                          |

## 首次使用
请使用[Mirai Console Backend - Permissions](https://docs.mirai.mamoe.net/console/Permissions.html)
设置权限

### 添加一个被监听者
> 当被监听者发送消息后会被缓存或转发
```
perm add m<群号>.* top.cutestar.antirecall:monitored
//一个示例:perm add m123456789.* top.cutestar.antirecall:monitored
```
监听所有群
```
perm add m* top.cutestar.antirecall:monitored
```

### 添加一个消息接收者
> 当出现选中类型的消息时，此消息会被转发给消息接收者
```
perm add <被许可人ID> top.cutestar.antirecall:receiver
//一个转发到好友的示例:perm add f123456789 top.cutestar.antirecall:receiver
//一个转发到群的示例:perm add g123456789 top.cutestar.antirecall:receiver
```

### 添加命令使用权限
```
perm add u<qq号> top.cutestar.antirecall:command.ar
```

设置权限后请使用`ar reload`重载

### 配置
> 请见配置文件注释
