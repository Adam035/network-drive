import { Routes, Route, Navigate } from "react-router-dom";
import { MainLayout } from "../layouts/MainLayout.tsx";
import { Login } from "../pages/Login.tsx";
import { Register } from "../pages/Register.tsx";
import { ProtectedRoute } from "../features/auth/components/ProtectedRoute.tsx";
import { Profile } from "../pages/Profile.tsx";

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<MainLayout />}>
                <Route element={<ProtectedRoute />}>
                    <Route index element={<Navigate to="profile" replace />} />
                    <Route path="profile" element={<Profile />} />
                </Route>
                <Route path="auth">
                    <Route path="login" element={<Login />} />
                    <Route path="register" element={<Register />} />
                </Route>
            </Route>
        </Routes>
    );
};
