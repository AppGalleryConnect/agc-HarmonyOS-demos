import { CredentialService } from '../credential-service/credential-service';
export declare class AGCClient {
    static INSTANCES: Map<string, AGCClient>;
    static DEFAULT_INSTANCE_NAME: string;
    private name;
    private credential;
    private constructor();
    getName(): string;
    getCredential(): CredentialService;
    static initialize(credential?: CredentialService, name?: string, region?: string): void;
    static getInstance(name?: string): AGCClient;
    private static overrideRegion;
    private static judgeEnv;
}
