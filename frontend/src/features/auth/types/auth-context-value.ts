import type { LoginResponse } from "./finish-login-response.ts";
import type {AuthTokensResponse} from "./auth-tokens-response.ts";

export interface AuthContextValue {
    accessToken: string;
    refreshToken: string;
    rotateAuthTokens: () => Promise<AuthTokensResponse | undefined>;
    login: () => Promise<LoginResponse | undefined>;
    logout: () => void;
    isInitializing: boolean;
}
