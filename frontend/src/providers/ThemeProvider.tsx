import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import type { Theme } from "../features/theme/types/theme.ts";
import type { ThemeContextValue } from "../features/theme/types/theme-context-value.ts";

const LIGHT_THEME = (import.meta.env.VITE_LIGHT_THEME ?? "cmyk") as Theme;
const DARK_THEME = (import.meta.env.VITE_DARK_THEME ?? "dim") as Theme;
const THEME_STORAGE_KEY = "app-theme";

const ThemeContext = createContext<ThemeContextValue | null>(null);
const ThemeConfigContext = createContext<{ lightTheme: Theme; darkTheme: Theme } | null>(null);

const getInitialTheme = (): Theme => {
    const saved = localStorage.getItem(THEME_STORAGE_KEY);
    if (saved === LIGHT_THEME || saved === DARK_THEME) return saved as Theme;

    const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
    return prefersDark ? DARK_THEME : LIGHT_THEME;
};

export const ThemeProvider = ({ children }: { children: React.ReactNode }) => {
    const [theme, setThemeState] = useState<Theme>(() => getInitialTheme());

    useEffect(() => {
        document.documentElement.setAttribute("data-theme", theme);
        localStorage.setItem(THEME_STORAGE_KEY, theme);
    }, [theme]);

    const setTheme = (nextTheme: Theme) => setThemeState(nextTheme);
    const toggleTheme = () => setThemeState((previous) => (previous === DARK_THEME ? LIGHT_THEME : DARK_THEME));
    const themeValue = useMemo(() => ({ theme, setTheme, toggleTheme }), [theme]);
    const configValue = useMemo(() => ({ lightTheme: LIGHT_THEME, darkTheme: DARK_THEME }), []);

    return (
        <ThemeConfigContext.Provider value={configValue}>
            <ThemeContext.Provider value={themeValue}>
                {children}
            </ThemeContext.Provider>
        </ThemeConfigContext.Provider>
    );
};

export const useTheme = (): ThemeContextValue => {
    const context = useContext(ThemeContext);
    if (!context) throw new Error("useTheme must be used inside ThemeProvider");

    return context;
};

export const useThemeConfig = () => {
    const context = useContext(ThemeConfigContext);
    if (!context) throw new Error("useThemeConfig must be used inside ThemeProvider");

    return context;
};
