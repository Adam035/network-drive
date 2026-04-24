import { Outlet, NavLink } from "react-router-dom";
import { ThemeToggleButton } from "../features/theme/components/ThemeToggleButton.tsx";

const APP_NAME = import.meta.env.VITE_APP_NAME;
const currentYear = new Date().getUTCFullYear();

export const MainLayout = () => {
    return (
        <div className="min-h-dvh flex flex-col bg-base-100 text-base-content">
            <header className="navbar bg-base-200 border-b border-base-300 px-4 md:px-6">
                <div className="w-full flex items-center justify-between">
                    <NavLink to="/" className="text-lg font-bold tracking-tight">
                        {APP_NAME}
                    </NavLink>

                    <nav className="flex items-center gap-2">
                        <NavLink to="/home" className="btn btn-ghost btn-sm">
                            Home
                        </NavLink>
                        <ThemeToggleButton />
                    </nav>
                </div>
            </header>

            <main className="flex-1 px-4 py-6 md:px-6 md:py-8">
                <div className="mx-auto w-full max-w-6xl">
                    <section className="p-4 md:p-6">
                        <Outlet />
                    </section>
                </div>
            </main>

            <footer className="footer footer-center bg-base-200 text-base-content border-t border-base-300 p-4">
                <aside>
                    <p className="text-sm">
                        <span className="font-semibold">{APP_NAME}</span> &#169; {currentYear}
                    </p>
                </aside>
            </footer>
        </div>
    );
};
