export declare class AccessToken {
    private token;
    private expirationTime;
    private static TWO_MINUTES_EARLY;
    constructor(token: string, expirationTime: number);
    getToken(): string;
    isValid(): boolean;
}
