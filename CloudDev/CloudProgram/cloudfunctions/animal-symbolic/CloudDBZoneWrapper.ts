import {AGConnectCloudDB, CloudDBZoneConfig, CloudDBZoneQuery} from "@agconnect/database-server/dist/index.js"
import {AGCClient, CredentialParser} from "@agconnect/common-server"
import path from 'path'
import {animal} from "./animal"

let object = new animal();

let logger

let mCloudDBZone

class CloudDBZoneWrapper {

  // AGC & 数据库初始化
  constructor(log) {

    let agcClient;

    const credentialPath = "/resources/agc-apiclient-1313739243687786368-7313826231207413268.json";
    try {
      agcClient = AGCClient.getInstance();
    } catch (error) {
      AGCClient.initialize(CredentialParser.toCredential(path.join(__dirname, credentialPath)));
      agcClient = AGCClient.getInstance();
    }

    AGConnectCloudDB.initialize(agcClient)

    const cloudDBZoneConfig = new CloudDBZoneConfig("cloudDBZoneName1");

    const agconnectCloudDB = AGConnectCloudDB.getInstance(agcClient);
    mCloudDBZone = agconnectCloudDB.openCloudDBZone(cloudDBZoneConfig);
  }

  // 插入数据
  async executeUpsert(data) {
    if (!mCloudDBZone) {
      console.log("CloudDBClient is null, try re-initialize it");
      return;
    }
    try {
      const resp = await mCloudDBZone.executeUpsert(data);
      console.log("CloudDB Success")
      return resp;
    } catch (error) {
      console.log("CloudDB error: " + error);
    }
  }
}

export default CloudDBZoneWrapper;