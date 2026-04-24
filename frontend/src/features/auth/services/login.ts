import type { LoginResponse } from "../types/finish-login-response.ts";
import { extractJwtPayload } from "../utils/extract-jwt-payload.ts";
import { finishLogin, startLogin } from "../api/login.ts";
import { getPublicKeyCredential } from "./credential.ts";

export const login = async (): Promise<LoginResponse | undefined> => {
    try {
        const response = await startLogin();
        const payload = extractJwtPayload(response.jwtToken);
        const publicKey = JSON.parse(payload.request).publicKeyCredentialRequestOptions;
        const credential = await getPublicKeyCredential(publicKey);

        if (credential) {
            return await finishLogin({
                publicKeyCredential: credential,
                jwtToken: response.jwtToken,
            });
        }
    } catch (error) {
        console.error(error);
    }
};
