import React, { createContext, useCallback, useContext, useEffect, useMemo, useState} from "react";
import type { LoginResponse } from "../features/auth/types/finish-login-response.ts";
import type { AuthTokensResponse } from "../features/auth/types/auth-tokens-response.ts";
import type { AuthContextValue } from "../features/auth/types/auth-context-value.ts";
import { login as loginRequest } from "../features/auth/services/login.ts";
import { rotateAuthTokens as rotateAuthTokensApi } from "../features/auth/api/auth-tokens.ts";

const AuthContext = createContext<AuthContextValue | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [accessToken, setAccessToken] = useState<string>("");
    const [refreshToken, setRefreshToken] = useState<string>("");
    const [isInitializing, setIsInitializing] = useState(true);

    const logout = useCallback(() => {
        setAccessToken("");
        setRefreshToken("");
    }, []);

    const rotateAuthTokens = useCallback(async (): Promise<AuthTokensResponse | undefined> => {
        try {
            const response = await rotateAuthTokensApi();
            if (!response) return;

            setAccessToken(response.accessToken);
            setRefreshToken(response.refreshToken);

            return response;
        } catch (error) {
            console.error("Failed to rotate auth tokens:", error);
            logout();
        }
    }, [refreshToken, logout]);

    useEffect(() => {
        const init = async () => {
            try {
                await rotateAuthTokens();
            } finally {
                setIsInitializing(false);
            }
        };
        void init();
    }, [rotateAuthTokens]);

    const login = useCallback(async (): Promise<LoginResponse | undefined> => {
        try {
            const response = await loginRequest();
            if (!response) return;

            setAccessToken(response.authTokens?.accessToken ?? "");
            setRefreshToken(response.authTokens?.refreshToken ?? "");

            return response;
        } catch (error) {
            console.error("Login failed:", error);
        }
    }, []);

    const value = useMemo<AuthContextValue>(
        () => ({ accessToken, refreshToken, rotateAuthTokens, login, logout, isInitializing }),
        [accessToken, refreshToken, rotateAuthTokens, login, logout, isInitializing],
    );

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);

    if (!context) throw new Error("useAuth must be used inside AuthProvider");

    return context;
};
