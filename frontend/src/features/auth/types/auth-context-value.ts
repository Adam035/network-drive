import type { User } from "./user.ts";
import type { LoginResponse } from "./finish-login-response.ts";

export interface AuthContextValue {
    user: User | null;
    login: () => Promise<LoginResponse | undefined>;
    logout: () => void;
}
