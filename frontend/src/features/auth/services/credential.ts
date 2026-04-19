export const createPublicKeyCredential = async (
    credentialCreationOptionsJson: PublicKeyCredentialCreationOptionsJSON
): Promise<Credential | null> => {
    const credentialCreationOptions = PublicKeyCredential.parseCreationOptionsFromJSON(credentialCreationOptionsJson);

    return await navigator.credentials.create({ publicKey: credentialCreationOptions });
};

export const getPublicKeyCredential = async (
    credentialRequestOptionsJson: PublicKeyCredentialRequestOptionsJSON
): Promise<Credential | null> => {
    const credentialRequestOptions = PublicKeyCredential.parseRequestOptionsFromJSON(credentialRequestOptionsJson);

    return await navigator.credentials.get({ publicKey: credentialRequestOptions });
};

