import { ErrorCode } from './error';
export declare class ErrorCodeConstant {
    static readonly FS_READ_FAIL: ErrorCode;
    static readonly FS_WRITE_FAIL: ErrorCode;
    static readonly URL_VALIDATE_FAIL: ErrorCode;
    static readonly MTTHOD_NOT_EXSIT: ErrorCode;
    static readonly URL_NOT_EXSIT: ErrorCode;
    static readonly FILE_NOT_EXSIT: ErrorCode;
    static readonly NOT_A_FILE: ErrorCode;
    static readonly FILE_NO_READ_PERMISSION: ErrorCode;
    static readonly ENV_NOT_NODEJS: ErrorCode;
    static readonly CREDENTIAL_ENV_NOT_SET: ErrorCode;
    static readonly LOAD_SERVICE_CONFIG_ERROR: ErrorCode;
    static readonly LOAD_CUSTOM_CONFIG_ERROR: ErrorCode;
    static readonly CREDENTIAL_PARSER: ErrorCode;
    static readonly AGC_CLIENT_PARA: ErrorCode;
    static readonly AGC_CLIENT_EXIST: ErrorCode;
    static readonly AGC_CLIENT_CREDENTIAL: ErrorCode;
    static readonly AGC_CLIENT_NOT_EXIST: ErrorCode;
    static readonly INVALID_REGION: ErrorCode;
    static readonly REQUEST_TOKEN_FAILED: ErrorCode;
    static readonly CREATE_HTTP_CLIENT_REPONSE_TYPE: ErrorCode;
    static toMessage(err: ErrorCode): string;
}
