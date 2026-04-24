export interface FinishRegistrationRequest {
    user: {
        email: string;
        username: string;
    };
    jwtToken: string;
    publicKeyCredential: Credential;
}
