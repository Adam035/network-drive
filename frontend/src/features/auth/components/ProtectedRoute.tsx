import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../../../providers/AuthProvider.tsx";
import { isAuthenticated } from "../services/authentication.ts";

export const ProtectedRoute = () => {
    const { accessToken, isInitializing } = useAuth();

    if (isInitializing) {
        return (
            <div className="flex items-center justify-center min-h-[60vh]">
                <span className="loading loading-spinner loading-md"></span>
            </div>
        );
    }

    if (!accessToken || !isAuthenticated(accessToken)) return <Navigate to="/auth/login" />;

    return <Outlet />;
};
