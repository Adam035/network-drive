export const extractJwtPayload = (jwt: string): any => {
    try {
        const payload = jwt.split(".")[1];
        return JSON.parse(atob(payload));
    } catch (error) {
        throw new Error(`Failed to extract credential options from JWT: ${error}`);
    }
};
