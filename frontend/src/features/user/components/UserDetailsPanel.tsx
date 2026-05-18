import React, { useEffect, useState } from "react";
import type { UserDetails } from "../types/user-details";
import { useAuth } from "../../../providers/AuthProvider";
import { getUserDetails } from "../api/get-user-details";
import { extractJwtPayload } from "../../auth/utils/extract-jwt-payload";
import {updateUser} from "../api/update-user.ts";

export const UserDetailsPanel = () => {
    const { accessToken } = useAuth();
    const [userDetails, setUserDetails] = useState<Omit<UserDetails, "id"> | null>(null);
    const [userForm, setUserForm] = useState<Omit<UserDetails, "id">>({ username: "", email: "" });
    const [isEditing, setIsEditing] = useState(false);

    const loadUser = async () => {
        if (!accessToken) return;

        const { sub: userId } = extractJwtPayload(accessToken);
        const data = await getUserDetails(userId, accessToken);

        if (!data) return;

        setUserDetails(data);
    };

    useEffect(() => void loadUser(), [accessToken]);

    useEffect(() => {
        if (userDetails) setUserForm(userDetails);
    }, [userDetails]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUserForm((previous) => ({ ...previous, [e.target.name]: e.target.value }));
    };

    const handleSave = async () => {
        const { sub: userId } = extractJwtPayload(accessToken);
        await updateUser(userId, accessToken, userForm)
        setIsEditing(false);
        await loadUser()
    };

    const handleCancel = () => {
        if (userDetails) setUserForm(userDetails);
        setIsEditing(false);
    };

    if (!userDetails) return;

    return (
        <>
            <div className="flex items-center justify-between">
                <h2 className="text-xl font-semibold">{userDetails.username}</h2>
                <span className="badge badge-outline">Details</span>
            </div>
            <div className="divider my-2"></div>
            <div className="space-y-3">
                <div className="flex items-center justify-between">
                    <span className="text-sm opacity-70">Username</span>
                    <input
                        name="username"
                        className="input input-bordered input-sm w-56 text-base disabled:text-base-content"
                        value={userForm.username}
                        disabled={!isEditing}
                        onChange={handleChange}
                    />
                </div>
                <div className="flex items-center justify-between">
                    <span className="text-sm opacity-70">e-mail</span>
                    <input
                        name="email"
                        className="input input-bordered input-sm w-56 text-base disabled:text-base-content"
                        value={userForm.email}
                        disabled={!isEditing}
                        onChange={handleChange}
                    />
                </div>
            </div>

            <div className="card-actions justify-end mt-4">
                {isEditing ? (
                    <>
                        <button className="btn btn-ghost btn-sm" onClick={handleCancel}>
                            Cancel
                        </button>
                        <button className="btn btn-primary btn-sm" onClick={handleSave}>
                            Save
                        </button>
                    </>
                ) : (
                    <button className="btn btn-primary btn-sm" onClick={() => setIsEditing(true)}>
                        Edit
                    </button>
                )}
            </div>
        </>
    );
};
