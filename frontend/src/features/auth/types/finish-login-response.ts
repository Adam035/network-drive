import type { User } from "./user.ts";

export interface FinishLoginResponse {
    isLoginSuccessful: boolean;
    message: string;
    user?: User;
}

export type LoginResponse = FinishLoginResponse;
