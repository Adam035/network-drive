import type { AuthTokensResponse } from "../types/auth-tokens-response.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const rotateAuthTokens = async (): Promise<AuthTokensResponse> => {
    const response = await fetch(`${BACKEND_URL}/api/auth/token/rotate`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
    });

    return await response.json() as Promise<AuthTokensResponse>;
};
