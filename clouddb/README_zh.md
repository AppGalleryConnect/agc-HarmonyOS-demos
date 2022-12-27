#  Cloud DB OpenHarmony Demo


## 概要简介
本项目是使用Cloud DB JS SDK开发的快速入门示例。

## 快速开始
- 在[AppGallery Connect](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/myProject) 页面上，创建一个项目并添加名为`QuickStartDemo`的web应用。

- 单击导航栏上的**认证服务**，启用匿名帐户身份验证。

- 推荐使用 [HUAWEI DevEco Studio](https://developer.harmonyos.com/en/docs/documentation/doc-guides/ide_versions_overview-0000001356521213) 来打开本项目。

- 单击导航栏上的**云数据库**，启用数据库服务。然后执行以下操作：

  （1）通过导入项目根目录下**app-schema.json**中的模板文件来创建对象类型。或者手动创建一个名为**BookInfo**的对象类型，并确保所有字段必须与项目中**BookInfo.js**中的字段相同。

  ```
  entry/src/main/ets/Service/app-schema.json
  entry/src/main/ets/Service/BookInfo.js
  ```

  （2） 在存储区页面创建名为`QuickStartDemo`的存储区。


- 在**项目设置**页面，获取应用配置信息。将其保存到`agconnect-services.json`文件中，文件路径为`entry/src/main/resources/rawfile/`。


- 集成Cloud DB SDK。

  （1）执行以下命令，在工程根目录下安装OpenHarmony编译构建工具。

    ```
    npm config set @ohos:registry=https://repo.harmonyos.com/npm/
    npm install
    ```

  （2）执行以下命令，在工程中安装Cloud DB JavaScript SDK服务模块。

    ```
    cd entry
    npm install
    ```
