import type { OAuth2Response } from "../types/oauth2-response.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const authorize = async (params: URLSearchParams): Promise<OAuth2Response> => {
    const response = await fetch(`${BACKEND_URL}/api/auth/oauth2`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({
            "client_id": params.get("client_id"),
            "response_type": params.get("response_type"),
            "code_challenge": params.get("code_challenge"),
            "code_challenge_method": params.get("code_challenge_method"),
        }),
    });



    return await response.json() as Promise<OAuth2Response>;
}
