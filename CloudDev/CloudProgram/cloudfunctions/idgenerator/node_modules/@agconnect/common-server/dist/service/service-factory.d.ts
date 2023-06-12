import { AGCService, factory } from "./agc-service";
import { AGCClient } from "../agc-client/agc-client";
export declare class ServiceFactory {
    private static SERVICE_INSTANCE_MAP;
    static initializeService(client: AGCClient, serviceName: string, fact: factory, configPath: string, ...args: any): AGCService;
}
