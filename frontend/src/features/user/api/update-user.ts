import type { UserDetails } from "../types/user-details.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const updateUser = async (
    userId: string,
    accessToken: string,
    userDetails: Omit<UserDetails, "id">,
): Promise<UserDetails> => {
    const response = await fetch(`${BACKEND_URL}/api/user/${userId}`, {
        method: "PUT",
        credentials: "include",
        headers: {
            "Authorization": `Bearer ${accessToken}`,
            "Content-Type": "application/json",
        },
        body: JSON.stringify(userDetails),
    });

    return await response.json() as Promise<UserDetails>;
};
