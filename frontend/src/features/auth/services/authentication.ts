import { extractJwtPayload } from "../utils/extract-jwt-payload.ts";

export const isAuthenticated = (accessToken: string): boolean => {
    const payload = extractJwtPayload(accessToken);
    const currentMillis = Math.floor(Date.now() / 1000);

    return payload.token_type === "access" && payload.exp > currentMillis;
};
