import type { User } from "./user.ts";

export interface FinishRegistrationResponse {
    user?: User;
    message: string;
}

export type RegistrationResponse = FinishRegistrationResponse;
