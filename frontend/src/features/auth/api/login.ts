import type { StartLoginResponse } from "../types/start-login-response.ts";
import type { FinishLoginRequest } from "../types/finish-login-request.ts";
import type { FinishLoginResponse } from "../types/finish-login-response.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const startLogin = async (): Promise<StartLoginResponse> => {
    const response = await fetch(`${BACKEND_URL}/auth/login/start`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
    });

    return await response.json() as Promise<StartLoginResponse>;
}

export const finishLogin = async (
    finishLoginRequest: FinishLoginRequest,
): Promise<FinishLoginResponse> => {
    const response = await fetch(`${BACKEND_URL}/auth/login/finish`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(finishLoginRequest),
    });

    return await response.json() as Promise<FinishLoginResponse>;
}
