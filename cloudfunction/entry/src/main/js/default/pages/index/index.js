import agconnect from '@agconnect/api';
import '@agconnect/instance';
import '@agconnect/function-harmony';
import prompt from '@system.prompt';
import fetch from '@system.fetch';
import storage from '@system.storage';

export default {
    data: {
        httpTrigger: "",
        reqBody: "",
        functionRes: ""
    },
    onInit() {
        var agConnectConfig =
            {
            };
        agconnect.instance().configInstance(agConnectConfig);
        agconnect.instance().setFetch(fetch);
        agconnect.instance().setStorage(storage);
    },
    httpTriggerChange(value) {
        this.httpTrigger = value.value;
    },
    reqBodyChange(value) {
        this.reqBody = value.value;
    },
    runfunction() {
        let functionCallable = agconnect.function().wrap(this.httpTrigger);
        functionCallable.call(this.reqBody).then((res) => {
            this.functionRes = JSON.stringify(res.getValue());
            prompt.showToast({
                message: 'run ok',
                duration: 2000,
            });
        }).catch(error => {
            console.error(error);
            prompt.showToast({
                message: 'run failed',
                duration: 2000,
            });
        });
    }
}
