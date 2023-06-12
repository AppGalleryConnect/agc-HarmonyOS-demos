export interface HttpClientCfg {
    timeout?: number;
    responseType?: string;
    maxBodyLength?: number;
    maxContentLength?: number;
    isEnableSSLCert?: boolean;
    casFile?: string[];
    commonHeaders?: {
        [key: string]: string;
    };
}
export interface HttpClientAPI {
    post(url: string, data: any, params: any, headers: any): Promise<any>;
    get(url: string, params: any, headers: any): Promise<any>;
    delete(url: string, data: any, params: any, headers: any): Promise<any>;
    put(url: string, data: any, params: any, headers: any): Promise<any>;
}
export declare class HttpClientAPIImpl implements HttpClientAPI {
    private static DEFAULT_TIMEOUT;
    private static DEFAULT_RESPONSE_TYPE;
    private instance;
    constructor(cfg?: HttpClientCfg);
    post(url: string, data: any, params: any, headers: any): Promise<any>;
    get(url: string, params: any, headers: any): Promise<any>;
    delete(url: string, data: any, params: any, headers: any): Promise<any>;
    put(url: string, data: any, params: any, headers: any): Promise<any>;
    private getReponseType;
    private send;
}
