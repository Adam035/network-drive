import * as React from "react";
import { useState } from "react";
import { register } from "../services/register.ts";

export const RegisterForm = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [userData, setUserData] = useState({
        email: "",
        username: "",
    });

    const handleRegister = async (event: React.SubmitEvent<HTMLFormElement>) => {
        event.preventDefault();
        setIsLoading(true);

        if (!userData) {
            alert("Please fill in all fields!");
            return;
        }

        try {
            const response = await register(userData);

            if (!response) return;

            if (response.user) {
                setSuccessMessage(response.message);
                setErrorMessage("");
                return;
            }
            setSuccessMessage("")
            setErrorMessage(response.message);
        } catch (error) {
            console.error(error);
            setErrorMessage("An error occurred during registration. Please try again later.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <form onSubmit={handleRegister} className="space-y-5">
            <div className="space-y-1">
                <h2 className="text-2xl font-bold text-base-content">Create account</h2>
                <p className="text-sm text-base-content/70">
                    Enter your details to continue with passkey registration
                </p>
            </div>

            <div className="form-control">
                <input
                    id="username"
                    type="text"
                    placeholder="username"
                    className="input input-bordered w-full"
                    value={userData.username}
                    onChange={e => setUserData({ ...userData, username: e.target.value })}
                    autoComplete="username"
                />
            </div>

            <div className="form-control">
                <input
                    id="email"
                    type="email"
                    placeholder="e-mail"
                    className="input input-bordered w-full"
                    value={userData.email}
                    onChange={e => setUserData({ ...userData, email: e.target.value })}
                    autoComplete="email"
                />
            </div>

            {errorMessage ? (
                <p className="text-error text-sm">{errorMessage}</p>
            ) : null}

            {successMessage ? (
                <p className="text-success text-sm">{successMessage}</p>
            ) : null}

            <div className="text-center">
                <button
                    type="submit"
                    className={`btn btn-primary px-10 ${isLoading ? "btn-disabled" : ""}`}
                    disabled={isLoading}
                >
                    {isLoading ? "Registering..." : "Register"}
                </button>
            </div>
        </form>
    );
};
