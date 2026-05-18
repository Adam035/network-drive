import type { UserDetails } from "../types/user-details.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const getUserDetails = async (userId: string, accessToken: string): Promise<UserDetails> => {
    const response = await fetch(`${BACKEND_URL}/api/user/${userId}`, {
        method: "GET",
        credentials: "include",
        headers: {
            "Authorization": `Bearer ${accessToken}`,
            "Content-Type": "application/json",
        },
    });

    return await response.json() as Promise<UserDetails>;
};
