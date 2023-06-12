export interface CredentialService {
    getProjectId(): string;
    getRegion(): string;
    getClientId(): string;
    getDeveloperId(): string;
    getClientSecret(): string;
    getAccessToken(): Promise<string>;
}
