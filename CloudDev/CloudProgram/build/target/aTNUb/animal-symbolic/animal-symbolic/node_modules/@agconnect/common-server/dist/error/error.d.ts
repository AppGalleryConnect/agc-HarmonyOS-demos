export declare class AGCBaseError extends Error {
    protected static readonly COLON = ": ";
    protected static readonly COMMA = ", ";
    protected static readonly DASH = "-";
    private readonly defaultName;
    constructor(msg: string, name?: string, suffix?: string);
    setName(value: string): void;
    getName(): string;
}
export interface ErrorCode {
    code: string;
    message: string;
}
export declare class AGCError extends AGCBaseError {
    private errorCode;
    constructor(errorCode: ErrorCode, name?: string, suffix?: string, msg?: string);
    getCode(): string;
    getMessage(): string;
}
