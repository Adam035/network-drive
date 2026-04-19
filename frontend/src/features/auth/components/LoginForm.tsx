import * as React from "react";
import { useState } from "react";
import { useAuth } from "../../../providers/AuthProvider.tsx";

export const LoginForm = () => {
    const { login } = useAuth();
    const [isLoading, setIsLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const handleLogin = async (event: React.SubmitEvent<HTMLFormElement>) => {
        event.preventDefault();
        setIsLoading(true);

        try {
            const response = await login();

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
            setErrorMessage("An error occurred during login. Please try again later.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <form onSubmit={handleLogin} className="space-y-5">
            <div className="space-y-1">
                <h2 className="text-2xl font-bold text-base-content">Log in</h2>
                <p className="text-sm text-base-content/70">
                    Log in to continue
                </p>
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
                    className={`btn btn-primary px-15 ${isLoading ? "btn-disabled" : ""}`}
                    disabled={isLoading}
                >
                    {isLoading ? "Logging in..." : "Log in"}
                </button>
            </div>
        </form>
    );
};
