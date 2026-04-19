import { useTheme, useThemeConfig } from "../../../providers/ThemeProvider.tsx";

export const ThemeToggleButton = () => {
    const { theme, toggleTheme } = useTheme();
    const { lightTheme } = useThemeConfig();

    return (
        <button type="button" className="btn btn-ghost" onClick={toggleTheme}>
            {theme === lightTheme ? "\u{2600}\u{fe0f}" : "\u{1f319}"}
        </button>
    );
};
