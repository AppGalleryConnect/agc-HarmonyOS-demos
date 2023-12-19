export declare class CloudGwUrlUtil {
    private static PROJECT_CLOUDGW_URL;
    private static TEAM_CLOUDGW_URL;
    private static TEAM_CLOUDGW_BACK_URL;
    static checkUseBackUrl(err: any, enableBackurl?: boolean): boolean;
    static getCloudgwUrlByRegion(region?: string, useBackup?: boolean): string;
}
