import { Routes, Route } from "react-router-dom";
import { MainLayout } from "../layouts/MainLayout.tsx";
import { Home } from "../pages/Home.tsx";
import { Login } from "../pages/Login.tsx";
import { Register } from "../pages/Register.tsx";

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<MainLayout />}>
                <Route path="home" element={<Home />} />
                <Route path="auth">
                    <Route path="login" element={<Login />} />
                    <Route path="register" element={<Register />} />
                </Route>
            </Route>
        </Routes>
    );
};
