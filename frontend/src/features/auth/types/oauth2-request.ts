export interface OAuth2Request {
    clientId: string | undefined;
    responseType: string | undefined;
    codeChallenge: string | undefined;
    codeChallengeMethod: string | undefined;
}
