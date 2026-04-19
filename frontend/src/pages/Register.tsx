import { RegisterForm } from "../features/auth/components/RegisterForm.tsx";
import { NavLink } from "react-router-dom";

const APP_NAME = import.meta.env.VITE_APP_NAME;

export const Register = () => {
    return (
        <div>
            <main className="px-4 py-8">
                <div className="mx-auto w-full max-w-5xl grid gap-6 lg:grid-cols-2">
                    <section className="card bg-base-200 shadow-xl border border-base-300">
                        <div className="card-body">
                            <div className="badge badge-primary badge-outline mb-2">Registration</div>
                            <h1 className="text-3xl font-bold">Create an account</h1>
                            <p className="text-base-content/70">
                                Create your account to unlock all {APP_NAME} features, keep your data secure, and enjoy
                                a faster sign-in experience with modern passkey authentication.
                            </p>
                        </div>
                    </section>

                    <section className="card bg-base-200 shadow-xl border border-base-300">
                        <div className="card-body">
                            <RegisterForm />
                        </div>
                    </section>
                </div>
            </main>

            <footer className="footer footer-center bg-base-100 text-base-content/70">
                <aside>
                    <p className="text-sm">Already have an account?&nbsp;
                        <NavLink to="/auth/login" className="tracking-tight underline">
                            Sign in
                        </NavLink>.
                    </p>
                </aside>
            </footer>
        </div>
    );
};
