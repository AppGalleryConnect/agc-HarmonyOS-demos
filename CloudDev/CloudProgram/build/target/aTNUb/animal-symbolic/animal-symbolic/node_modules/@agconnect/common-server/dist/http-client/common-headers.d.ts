export declare class CommonHeaders {
    static DEFAULT_CONFIG: string;
    static KEY_REQUEST_ID: string;
    static KEY_USER_AGENT: string;
    static SDK_NAME: string;
    static SDK_VERSION: string;
    static ALGORITHM_TYPE_NEW_JWT_TOKEN: string;
    static SDK_PREFIX: string;
    static USER_AGENT_PREFIX: string;
    static KEY_AUTHORIZATION: string;
    static VALUE_BEARER: string;
    static KEY_HOST: string;
    static generateRequestId(): string;
    static getUserAgent(): string;
    static getSDKName(serviceName?: string): string;
    static getSDKVersion(): string;
}
