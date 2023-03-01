## 云存储快速入门

中文 

## 目录

 * [简介](#简介)
 * [环境要求](#环境要求)
 * [快速入门](#快速入门)
 * [示例代码](#示例代码)
 * [技术支持](#技术支持)
 * [授权许可](#授权许可)

## 简介
云存储服务提供简单易用、功能强大的端/云SDK，实现用户生成内容的存储，让您无需关注云端能力的构建而聚焦业务逻辑开发。

## 环境要求
* 配置好OpenHarmony的编译环境

## 快速入门
在运行quickstart前，您需要
1. 如果没有华为开发者联盟帐号，需要先[注册账号](https://developer.huawei.com/consumer/cn/doc/start/registration-and-verification-0000001053628148) 并通过实名认证。
2. 使用申请的帐号登录[AppGallery Connect](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/) 网站创建一个项目并添加应用，软件包类型选择“HarmonyOS”。
3. 在我的项目中进入新建的项目，选择创建的鸿蒙应用，进入“构建”>“云存储”页面，点击“立即开通”，开启云存储。
4. 在“项目设置”的“常规”页面，下载agconnect-services.json文件，放置在AppScope/resources/rawfile目录下
5. 回到“构建”>“云存储”页面
6. 在命令行中依次运行如下命令以运行demo：
    ```
    # 安装依赖
    npm install

    # 安装 AGC cloud cloudstorage ohos sdk
    npm install @hw-agconnect/cloudstorage-ohos


## 示例代码

Sample code: entry\src\main\ets\*


## 技术支持

如果您对使用AppGallery Connect示例代码有疑问，请通过如下途径寻求帮助：
- 访问[Stack Overflow](https://stackoverflow.com/) , 在`AppGallery`标签下提问，有华为研发专家在线一对一解决您的问题。
- 访问[华为开发者论坛](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Connect板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/AppGalleryConnect/agc-demos/issues) ，也欢迎您提交[Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) 。

## 授权许可
该示例代码经过[Apache 2.0 授权许可](http://www.apache.org/licenses/LICENSE-2.0) 。

