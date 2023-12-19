import { ConnectRet } from "./connect-ret";
export declare abstract class BaseResponse {
    ret: ConnectRet;
    isSuccess(): boolean;
    getRet(): ConnectRet;
    setRet(value: ConnectRet): void;
    abstract constructResponse(response: any): void;
}
