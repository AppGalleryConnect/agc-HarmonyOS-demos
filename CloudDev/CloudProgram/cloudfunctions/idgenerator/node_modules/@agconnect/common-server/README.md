# Huawei AppGallery Connect Node.js Server SDK


## Table of Contents

 * [Overview](#overview)
 * [Installation](#installation)
 * [Documentation](#documentation)
 * [Supported Environments](#supported-environments)
 * [License](#license)


## Overview

In many cases, you need to access 
Huawei services from your server. For example, you need to manage users on 
your management console or operate the database when processing service access. 
AppGallery Connect provides the Server SDK for you to integrate it into your 
server for function development. 

For more information, visit the
[AppGallery Connect Introduction](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-introduction-0000001057492641).


## Installation

The AppGallery Connect Node.js SDK is available on npm as `@agconnect/common-server`:

```bash
$ npm install --save @agconnect/common-server
```

To use the module in your application, you can `require` it from any JavaScript file:

```js
var { AGCClient, CredentialParser }= require("@agconnect/common-server")
```

If you are using ES6, you can also `import` the module:

```js
import {AGCClient, CredentialParser} from "@agconnect/common-server";
```


## Supported Environments

We support Node.js 10.12.0 and higher.

Also note that the Huawei AppGallery Connect Node.js Server SDK 
should only be used in server-side/back-end environments controlled 
by the application developer.This includes most server and serverless platforms. 
Do not use the Node.js Server SDK environment in the client.


## Documentation

* [Getting Started with Server](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-get-started-server-0000001058092593)


## License

Huawei AppGallery Connect Node.js Server SDK is licensed under the "ISC".
