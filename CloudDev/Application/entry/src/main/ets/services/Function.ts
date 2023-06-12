import agconnect from '@hw-agconnect/api-ohos';
import "@hw-agconnect/function-ohos";

import { Log } from '../common/Log';
import { getAGConnect } from './AgcConfig';

const TAG = "[AGCFunction]";

export function uuid(context, year): Promise<string> {
        return new Promise((resolve, reject) => {
            getAGConnect(context);
            let functionResult;
            let functionCallable = agconnect.function().wrap("animal-symbolic-$latest");
            let params = {"year" : year};
            functionCallable.call(params).then((ret: any) => {
                functionResult = ret.getValue();
                Log.info(TAG, "Cloud Function Called, Returned Value: " + JSON.stringify(ret.getValue()));
                resolve(functionResult.result);
            }).catch((error: any) => {
                Log.error(TAG, "Error - could not obtain cloud function result. Error Detail: " + JSON.stringify(error));
                reject(error);
            });
        });
}