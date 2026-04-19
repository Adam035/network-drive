import type { FinishRegistrationResponse } from "../types/finish-registration-response.ts";
import type { UserCreationRequest } from "../types/user-creation-request.ts";
import { finishRegistration, startRegistration } from "../api/register.ts";
import { extractJwtPayload } from "../utils/extract-jwt-payload.ts";
import { createPublicKeyCredential } from "./credential.ts";

export const register = async (
    userCreationRequest: UserCreationRequest,
): Promise<FinishRegistrationResponse | undefined> => {
    try {
        const response = await startRegistration({ user: userCreationRequest });
        const payload = extractJwtPayload(response.jwtToken);
        const credential = await createPublicKeyCredential(JSON.parse(payload.request));

        if (credential) {
            return await finishRegistration({
                user: userCreationRequest,
                jwtToken: response.jwtToken,
                publicKeyCredential: credential,
            });
        }
    } catch (error) {
        console.error(error);
    }
};
