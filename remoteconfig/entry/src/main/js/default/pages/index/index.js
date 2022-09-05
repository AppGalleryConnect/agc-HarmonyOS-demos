import agconnect from '@agconnect/api';
import '@agconnect/instance';
import '@agconnect/remoteconfig-harmony';
import app from '@system.app';
import configuration from '@system.configuration';
import fetch from '@system.fetch';
import storage from '@system.storage';
export default {
    data: {
        title: "",
        fetchReqTimeoutMillis: "",
        minFetchIntervalMillis: "",
        defaultConfig: "",
        configInfo: ""
    },
    changeFetchReqTimeoutMillis(value) {
        this.fetchReqTimeoutMillis = value.value;
    },
    changeMinFetchIntervalMillis(value) {
        this.minFetchIntervalMillis = value.value;
    },
    changeDefaultConfig(value) {
        this.defaultConfig = value.value;
    },
    onInit() {
        var agConnectConfig =
            {
            };
        agconnect.instance().configInstance(agConnectConfig);
        agconnect.instance().setFetch(fetch);
        agconnect.instance().setStorage(storage);
        agconnect.instance().setApp(app);
        agconnect.instance().setConfiguration(configuration);
    },
    fetchAndApply() {
        if (this.fetchReqTimeoutMillis) {
            agconnect.remoteConfig().fetchReqTimeoutMillis = this.fetchReqTimeoutMillis;
        }
        if (this.minFetchIntervalMillis) {
            agconnect.remoteConfig().minFetchIntervalMillis = this.minFetchIntervalMillis;
        }
        agconnect.remoteConfig().fetch().then(() => {
            agconnect.remoteConfig().apply();
        })
    },
    showConfig() {
        if (this.defaultConfig) {
            var defaultConfigJson = JSON.parse(this.defaultConfig);
            if (defaultConfigJson && typeof defaultConfigJson == 'object') {
                let defaultConfigMap = new Map();
                for (var k in defaultConfigJson) {
                    defaultConfigMap.set(k, JSON.stringify(defaultConfigJson[k]));
                }
                agconnect.remoteConfig().applyDefault(defaultConfigMap);
            }
        }

        let obj = '';
        let resultMap = agconnect.remoteConfig().getMergedAll();
        resultMap.forEach(function (value, key) {
            obj = obj + key + ': ( value:' + value.getValueAsString() + ', source:' + value.getSource() + ' )\n';
        });
        this.configInfo = obj;
    }
}
