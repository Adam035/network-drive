import type { AuthTokensResponse } from "./auth-tokens-response.ts";

export interface FinishLoginResponse {
    authTokens?: AuthTokensResponse;
    message: string;
}

export type LoginResponse = FinishLoginResponse;
