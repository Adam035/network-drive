import { useAuth } from "../../providers/AuthProvider.tsx";

export const fetchWithInterceptor = async (url: string, request: Request): Promise<Response> => {
    const response = await fetch(url, request);
    const { refreshToken } = useAuth();

    if (!refreshToken || response.status !== 401) return response

    const authTokens = await useAuth().rotateAuthTokens();

    if (!authTokens) return response;

    return fetch(url, {
        ...request,
        headers: {
            ...(request?.headers ?? {}),
            Authorization: `Bearer ${authTokens.accessToken}`,
        },
    });
};
