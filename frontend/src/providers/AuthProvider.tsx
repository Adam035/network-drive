import React, { createContext, useContext, useMemo, useState } from "react";
import type { User } from "../features/auth/types/user.ts";
import type { LoginResponse } from "../features/auth/types/finish-login-response.ts";
import type { AuthContextValue } from "../features/auth/types/auth-context-value.ts";
import { login as loginRequest } from "../features/auth/services/login.ts";

const AuthContext = createContext<AuthContextValue | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);

    const login = async (): Promise<LoginResponse | undefined> => {
        try {
            const response = await loginRequest()
            if (!response) return;

            setUser(response.user ?? null);

            return response;
        } catch (error) {
            console.error("Login failed:", error);
        }
    };

    const logout = () => setUser(null);

    const value = useMemo<AuthContextValue>(
        () => ({ user, login, logout }),
        [user]
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
