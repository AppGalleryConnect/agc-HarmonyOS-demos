export interface RequestConstructor {
    getUrl(useBackUrl?: boolean): string;
    getHeader(): any;
    getBody(): any;
}
