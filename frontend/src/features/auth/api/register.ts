import type { StartRegistrationRequest } from "../types/start-registration-request.ts";
import type { StartRegistrationResponse } from "../types/start-registration-response.ts";
import type { FinishRegistrationRequest } from "../types/finish-registration-request.ts";
import type { FinishRegistrationResponse } from "../types/finish-registration-response.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const startRegistration = async (
    startRegistrationRequest: StartRegistrationRequest,
): Promise<StartRegistrationResponse> => {
    const response = await fetch(`${BACKEND_URL}/auth/register/start`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(startRegistrationRequest),
    });

    return await response.json() as Promise<StartRegistrationResponse>;
}

export const finishRegistration = async (
    finishRegistrationRequest: FinishRegistrationRequest,
): Promise<FinishRegistrationResponse> => {
    const response = await fetch(`${BACKEND_URL}/auth/register/finish`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(finishRegistrationRequest),
    });

    return await response.json() as Promise<FinishRegistrationResponse>;
}
