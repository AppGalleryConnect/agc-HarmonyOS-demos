/*
* Copyright 2022. Huawei Technologies Co., Ltd. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import { BookInfo } from './BookInfo';
import {
    AGConnectCloudDB,
    CloudDBZoneConfig,
    CloudDBZone,
    CloudDBZoneQuery
} from '@hw-agconnect/database-ohos';
// @ts-ignore
import * as schema from './app-schema.json';

import agconnect from '@hw-agconnect/api-ohos';
import "@hw-agconnect/core-ohos";
import "@hw-agconnect/auth-ohos";
import { PhoneAuthProvider } from "@hw-agconnect/auth-ohos";
import { AGCRoutePolicy } from "@hw-agconnect/core-ohos";

export class CloudDBService {
    private static readonly ZONE_NAME = 'QuickStartDemo';
    private static isLogin: boolean = false;
    private static cloudDB: AGConnectCloudDB;
    private static cloudDBZone: CloudDBZone;
    private static isInit: boolean;

    public static getLogin(): boolean {
        return this.isLogin;
    }

    public static async init(context: any): Promise<boolean> {
        if (this.isInit) {
            return;
        }
        try {
            // agc init
            agconnect.instance().init(context);
            console.log('agconnect.instance() success.')
            // Cloud DB init
            await AGConnectCloudDB.initialize(context);
            console.log('AGConnectCloudDB.initialize success.')
            this.cloudDB = await AGConnectCloudDB.getInstance(AGCRoutePolicy.CHINA);
            console.log('this.cloudDB=' + this.cloudDB)
            // import schema
            this.cloudDB.createObjectType(schema);
            console.log('createObjectType success.');
            await this.openZone(this.ZONE_NAME);
            this.isInit = true;
        } catch (e) {
            console.error('init failed.')
        }
        return Promise.resolve(this.isInit);
    }

    public static async login(username: string, password: string): Promise<boolean> {
        let credential = PhoneAuthProvider.credentialWithPassword("86", username, password);
        const user = await agconnect.auth().getCurrentUser();
        if (user) {
            console.log('user has logged in.')
            this.isLogin = true;
            return Promise.resolve(true);
        }
        try {
            const signInResult = await agconnect.auth().signIn(credential);
            if (signInResult) {
                console.log('login success.')
                this.isLogin = true;
                return Promise.resolve(true);
            }
            this.isLogin = false;
            return Promise.resolve(false);
        } catch (e) {
            console.error('login failed. error' + JSON.stringify(e));
            this.isLogin = false;
            return Promise.resolve(false);
        }
    }

    private static async openZone(zoneName: string): Promise<CloudDBZone> {
        if (this.cloudDBZone) {
            console.log('zone has been closed.')
            return;
        }
        try {
            const cloudDBZoneConfig = new CloudDBZoneConfig(zoneName);
            this.cloudDBZone = await this.cloudDB.openCloudDBZone(cloudDBZoneConfig);
            console.log('[openZone] open zone success.')
        } catch (e) {
            console.error('[openZone] open zone failed.' + e);
            console.error(JSON.stringify(e));
        }
    }

    public static async closeZone(): Promise<void> {
        try {
            this.cloudDB.closeCloudDBZone(this.cloudDBZone);
            console.log('[closeZone] close zone success.');
            this.cloudDBZone = undefined;
        } catch (e) {
            console.error('[closeZone] close zone failed!' + e);
            console.error(JSON.stringify(e));
        }
    }

    public static async upsertRecord(books: BookInfo | Array<BookInfo>): Promise<number> {
        try {
            const upsertNum = await this.cloudDBZone.executeUpsert(books);
            console.log('[upsertRecord] upsertNum=' + upsertNum);
            return upsertNum;
        } catch (e) {
            console.error('[upsertRecord] upsertRecord failed.' + JSON.stringify(e));
            return 0;
        }
    }

    public static async deleteRecord(books: BookInfo | Array<BookInfo>): Promise<number> {
        try {
            const deleteNum = await this.cloudDBZone.executeDelete(books);
            console.log('[deleteRecord] deleteNum=' + deleteNum);
            return deleteNum;
        } catch (e) {
            console.error('[deleteRecord] deleteRecord failed.' + e);
            return 0;
        }
    }

    public static async queryBooks(queryStr?: string): Promise<Array<BookInfo>> {
        try {
            const query = CloudDBZoneQuery.where(BookInfo);
            if (queryStr) {
                query.contains('bookName', queryStr).or().contains('author', queryStr);
            }
            const result = await this.cloudDBZone.executeQuery(query);
            console.log('[queryBooks] query books=' + JSON.stringify(result.getSnapshotObjects()));
            return result.getSnapshotObjects();
        } catch (e) {
            console.error('[queryBooks] queryBooks failed.' + JSON.stringify(e));
        }
    }
}