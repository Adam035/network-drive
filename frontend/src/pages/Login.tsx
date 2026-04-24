import { NavLink } from "react-router-dom";
import { LoginForm } from "../features/auth/components/LoginForm.tsx";

export const Login = () => {
    return (
        <div>
            <main className="px-4 py-8">
                <div className="mx-auto w-full max-w-5xl grid gap-6 lg:grid-cols-2">
                    <section className="card bg-base-200 shadow-xl border border-base-300">
                        <div className="card-body">
                            <div className="badge badge-primary badge-outline mb-2">Log in</div>
                            <h1 className="text-3xl font-bold">Welcome back</h1>
                            <p className="text-base-content/70">
                                Log in to your account to continue with the app.
                            </p>
                        </div>
                    </section>

                    <section className="card bg-base-200 shadow-xl border border-base-300">
                        <div className="card-body">
                            <LoginForm />
                        </div>
                    </section>
                </div>
            </main>

            <footer className="footer footer-center bg-base-100 text-base-content/70">
                <aside>
                    <p className="text-sm">Don't have an account?&nbsp;
                        <NavLink to="/auth/register" className="tracking-tight underline">
                            Sign up
                        </NavLink>.
                    </p>
                </aside>
            </footer>
        </div>
    );
};
