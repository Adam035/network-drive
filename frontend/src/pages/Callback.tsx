import { useLocation } from "react-router-dom";

export const Callback = () => {
    const { search } = useLocation();
    const params = new URLSearchParams(search);
    const code = params.get("code");
    const state = params.get("state");
    const isSuccessful = code !== "null" && state !== "null";

    if (isSuccessful) window.location.assign(`network-drive://callback?code=${code}&state=${state}`);

    return (
        <div>
            <main className="px-4 py-8">
                <section className="card mx-auto w-full max-w-xl border border-base-300 bg-base-200 shadow-xl">
                    <div className="card-body items-start">
                        <div
                            className={`badge mb-2 ${isSuccessful ? "badge-success" : "badge-error"}`}
                        >
                            Authentication
                        </div>

                        <h1 className="text-3xl font-bold">
                            {isSuccessful ? "Authentication successful" : "Authentication failed"}
                        </h1>

                        <p className="text-base-content/70">
                            {isSuccessful
                                ? "You have successfully authenticated"
                                : "We couldn't complete your authentication attempt"}
                        </p>
                    </div>
                </section>
            </main>

            <footer className="footer footer-center bg-base-100 text-base-content/70">
                <aside>
                    <p className="text-sm">You can close this page now</p>
                </aside>
            </footer>
        </div>
    );
};
