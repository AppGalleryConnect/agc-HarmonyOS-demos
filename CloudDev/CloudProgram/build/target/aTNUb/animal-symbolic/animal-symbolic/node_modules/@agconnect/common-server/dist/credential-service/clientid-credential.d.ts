import { CredentialService } from './credential-service';
export declare class ClientIdCredential implements CredentialService {
    private projectId;
    private region;
    private developerId;
    private type;
    private clientId;
    private clientSecret;
    private configurationVersion;
    private accessToken;
    private static CLIENT_TOKEN_PATH;
    constructor(projectId: string, region: string, developerId: string, type: string, clientId: string, clientSecret: string, configurationVersion: string);
    getProjectId(): string;
    getRegion(): string;
    getDeveloperId(): string;
    getType(): string;
    getClientId(): string;
    getClientSecret(): string;
    getConfigurationVersion(): string;
    setRegion(region: string): void;
    getAccessToken(): Promise<string>;
    refreshAccessToken(): Promise<string>;
    private getGWTokenUrl;
}
