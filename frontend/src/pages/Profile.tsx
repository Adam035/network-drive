import {UserDetailsPanel} from "../features/user/components/UserDetailsPanel.tsx";


export const Profile = () => {
    return (
        <div className="flex items-center justify-center">
            <div className="card bg-base-200 shadow-xl border border-base-300 w-full max-w-xl">
                <div className="card-body">
                    <UserDetailsPanel />
                </div>
            </div>
        </div>
    );
};
