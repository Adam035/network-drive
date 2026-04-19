import type {UserCreationRequest} from "./user-creation-request.ts";

export interface StartRegistrationRequest {
    user: UserCreationRequest,
}
