#  Cloud DB OpenHarmony Demo


## Introduction
This project is a quick start sample developed using Cloud DB SDK.

##  Quick Start
- On the [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/myApp) page, create a project and add an application with named QuickStartDemo.

- Click **Auth Service** on the navigation bar and enable authentication using an anonymous account.


- It is recommended that you use the [HUAWEI DevEco Studio](https://developer.harmonyos.com/en/docs/documentation/doc-guides/ide_versions_overview-0000001356521213) to open the project.

- Click **Cloud DB** on the navigation bar and enable database service. Then, perform the following operations:

  （1）Create a schema by importing a template file stored in **app-schema.json** in the root directory of the project. Alternatively, create a schema named **BookInfo** and ensure that all fields must be the same as those in **BookInfo.js** in the project.

  ```
  entry/src/main/ets/Service/app-schema.json
  entry/src/main/ets/Service/BookInfo.js
  ```

  （2） Create a Cloud DB zone. On the **Cloud DB Zone** tab page, click **Add** to create a Cloud DB zone named **QuickStartDemo**.


- On the Project Setting page, obtain the app configuration information. Save it to the context object in the **agconnect-services.json** file. Then replace the file `AppScope/resources/rawfile/agconnect-services.json`.

- Integrate the Cloud DB SDK.

  （1）Run the following command to install the OpenHarmony compilation and build tool in the root directory of the project.

    ```
    npm config set @ohos:registry=https://repo.harmonyos.com/npm/
    npm install
    ```

  （2）Run the following command to install the Cloud DB JavaScript SDK service module in the project.

    ```
    cd entry
    npm install
    ```
  
