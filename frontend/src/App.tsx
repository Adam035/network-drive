import { BrowserRouter } from "react-router-dom";
import { ThemeProvider } from "./providers/ThemeProvider.tsx";
import { AuthProvider } from "./providers/AuthProvider.tsx";
import { AppRoutes } from "./routes/AppRoutes.tsx";

const App = () => {
  return (
      <BrowserRouter>
          <ThemeProvider>
              <AuthProvider>
                  <AppRoutes />
              </AuthProvider>
          </ThemeProvider>
      </BrowserRouter>
  );
};

export default App;
