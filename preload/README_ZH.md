# 预加载服务

## 介绍

本示例展示了HarmonyOS应用/元服务使用预加载服务的方法。

需要使用@hms.core.deviceCloudGateway.cloudFunction。

## Sample工程使用说明

本工程演示了在应用启动阶段使用云函数调用接口时，通过loadMode参数的PRELOAD模式获取预加载服务缓存的应用数据（比如首屏布局文件），以及当获取缓存数据失败后通过NORMAL模式调用云函数获取应用数据的流程。

## 工程目录
├─entry/src/main/ets   // 代码区  
│ ├─entryability                
│ │ └─EntryAbility.ets // 程序入口类  
│ ├─pages              // 存放页面文件目录                
│ │ └─Index.ets // 入口类文件

## 具体实现

使用@hms.core.deviceCloudGateway.cloudFunction的call方法时设置loadMode为PRELOAD，以Promise方式返回获取到的预加载服务缓存中的数据。
当获取缓存数据失败后，可以把loadMode设置为NORMAL或者删除loadMode参数（默认为NORMAL），直接从云服务中获取数据。

可参考：entry\src\main\ets\entryability\EntryAbility.ets
## 相关权限

该Sample应用在调用接口时需要访问网络权限，已在module.json5文件中添加。

访问网络权限："ohos.permission.INTERNET"。

## 依赖

设备需具备WIFI能力

## 约束与限制

1. 本示例仅支持标准系统上运行，支持设备：华为手机、平板。
2. 本示例支持API version 12。
3. 本示例需要使用的HarmonyOS版本：HarmonyOS NEXT Developer  Beta1及以上 。 
4. 本示例需要使用DevEco Studio NEXT Developer  Beta1及以上版本进行编译运行 。

