import { authorize } from "../api/oauth2.ts";
import type {OAuth2Response} from "../types/oauth2-response.ts";

export const isOAuth2 = (): boolean => {
    const params = new URLSearchParams(window.location.search);

    return ["client_id", "redirect_uri", "response_type", "code_challenge", "code_challenge_method", "state"]
        .every(key => params.has(key));
};

export const runOAuth2Flow = async (): Promise<OAuth2Response> => {
    try {
        const params = new URLSearchParams(window.location.search);
        const { code } = await authorize(params);
        const state = params.get("state") || "";

        return { code, state };
    } catch (error) {
        console.error(error);

        return { code: null, state: null };
    }
};
