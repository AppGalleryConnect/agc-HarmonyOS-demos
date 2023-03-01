## 认证服务快速入门

中文 | [English](./README.md)

## 目录

 * [简介](#简介)
 * [环境要求](#环境要求)
 * [快速入门](#快速入门)
 * [示例代码](#示例代码)
 * [技术支持](#技术支持)
 * [授权许可](#授权许可)

## 简介
大部分的应用都需要对用户的业务访问进行身份认证，以便为用户提供个性化体验。但是端到端完整构建一套用户认证系统会面临很多挑战。认证服务可以为您的应用快速构建安全可靠的用户认证系统，您只需在应用中访问认证服务的相关能力，而不需要关心云侧的设施和实现。

## 环境要求
* 配置好OpenHarmony的编译环境

## 快速入门
在运行quickstart前，您需要

1. 如果没有华为开发者联盟帐号，需要先注册账号 并通过实名认证。
2. 使用申请的帐号登录AppGallery Connect 网站创建一个项目并添加应用，软件包类型选择“HarmonyOS”。
3. 在我的项目中进入新建的项目，选择创建的鸿蒙应用，进入“构建”>“认证服务”页面，点击“立即开通”，开启认证服务。
4. 在“项目设置”的“常规”页面，下载agconnect-services.json文件，放置在AppScope/resources/rawfile目录下
5. 回到“构建”>“认证服务”页面，点击“认证方式”，启用“手机号码”、“邮箱地址”。
6. 在命令行中依次运行如下命令以运行demo：
    ```
    # 安装依赖
    npm install

    # 安装 AGC auth ohos sdk
    npm install @hw-agconnect/auth-ohos
   
    ```

## 示例代码

Sample code: entry\src\main\ets\*


## 技术支持

如果您对使用AppGallery Connect示例代码有疑问，请通过如下途径寻求帮助：
- 访问[Stack Overflow](https://stackoverflow.com/) , 在`AppGallery`标签下提问，有华为研发专家在线一对一解决您的问题。
- 访问[华为开发者论坛](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Connect板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/AppGalleryConnect/agc-demos/issues) ，也欢迎您提交[Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) 。

## 授权许可
该示例代码经过[Apache 2.0 授权许可](http://www.apache.org/licenses/LICENSE-2.0) 。

